package com.marcelo.beagleandroidtest.config

import br.com.zup.beagle.android.annotation.BeagleComponent
import br.com.zup.beagle.android.exception.BeagleApiException
import br.com.zup.beagle.android.networking.HttpClient
import br.com.zup.beagle.android.networking.HttpMethod
import br.com.zup.beagle.android.networking.RequestCall
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.networking.ResponseData
import com.marcelo.beagleandroidtest.getSafeError
import com.marcelo.beagleandroidtest.getSafeResponseCode
import com.marcelo.beagleandroidtest.getSafeResponseMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.EOFException
import java.net.HttpURLConnection
import java.net.URI

typealias OnSuccess = (responseData: ResponseData) -> Unit
typealias OnError = (responseData: ResponseData) -> Unit

@BeagleComponent
class HttpClientDefault : HttpClient, CoroutineScope {

    private val job = Job()
    override val coroutineContext = job + CoroutineDispatchers.IO


    override fun execute(
        request: RequestData,
        onSuccess: OnSuccess,
        onError: OnError,
    ): RequestCall {
        if (getOrDeleteOrHeadHasData(request)) {
            onError(ResponseData(-1, data = byteArrayOf()))
            return createRequestCall()
        }

        launch {
            try {
                val responseData = doHttpRequest(request)
                onSuccess(responseData)
            } catch (ex: BeagleApiException) {
                onError(ex.responseData)
            }
        }

        return createRequestCall()
    }

    private fun getOrDeleteOrHeadHasData(request: RequestData): Boolean {
        val method = request.httpAdditionalData.method
        val body = request.httpAdditionalData.body
        return (method == HttpMethod.GET ||
                method == HttpMethod.DELETE ||
                method == HttpMethod.HEAD) &&
                body != null
    }

    @Throws(BeagleApiException::class)
    private fun doHttpRequest(
        request: RequestData,
    ): ResponseData {
        val urlConnection: HttpURLConnection

        try {
            val uri = URI(request.url)
            urlConnection = uri.toURL().openConnection() as HttpURLConnection
        } catch (e: Exception) {
            throw BeagleApiException(ResponseData(-1, data = byteArrayOf()), request)
        }

        request.httpAdditionalData.headers?.forEach {
            urlConnection.setRequestProperty(it.key, it.value)
        }

        addRequestMethod(urlConnection, request.httpAdditionalData.method)

        val body = request.httpAdditionalData.body
        if (body != null) {
            setRequestBody(urlConnection, request)
        }

        try {
            return createResponseData(urlConnection)
        } catch (e: Exception) {
            throw tryFormatException(urlConnection, request)
        } finally {
            urlConnection.disconnect()
        }
    }

    private fun tryFormatException(urlConnection: HttpURLConnection, request: RequestData): BeagleApiException {
        val response = urlConnection.getSafeError() ?: byteArrayOf()
        val statusCode = urlConnection.getSafeResponseCode()
        val statusText = urlConnection.getSafeResponseMessage()
        val responseData = ResponseData(statusCode = statusCode,
            data = response, statusText = statusText)

        return BeagleApiException(responseData, request)
    }

    private fun addRequestMethod(urlConnection: HttpURLConnection, method: HttpMethod?) {
        val methodValue = method.toString()

        if (method == HttpMethod.PATCH || method == HttpMethod.HEAD) {
            urlConnection.setRequestProperty("X-HTTP-Method-Override", methodValue)
            urlConnection.requestMethod = "POST"
        } else {
            urlConnection.requestMethod = methodValue
        }
    }

    private fun setRequestBody(urlConnection: HttpURLConnection, request: RequestData) {
        urlConnection.setRequestProperty("Content-Length", request.httpAdditionalData.body?.length.toString())
        try {
            urlConnection.outputStream.write(request.httpAdditionalData.body?.toByteArray())
        } catch (e: Exception) {
            throw BeagleApiException(ResponseData(-1, data = byteArrayOf()), request)
        }
    }

    private fun createResponseData(urlConnection: HttpURLConnection): ResponseData {
        return ResponseData(
            statusCode = urlConnection.responseCode,
            statusText = urlConnection.responseMessage,
            headers = urlConnection.headerFields.filter { it.key != null }.map {
                val headerValue = it.value.toString()
                    .replace("[", "")
                    .replace("]", "")
                it.key to headerValue
            }.toMap(),
            data = try {
                urlConnection.inputStream.readBytes()
            } catch (e: EOFException) {
                byteArrayOf()
            }
        )
    }

    private fun createRequestCall() = object : RequestCall {
        override fun cancel() {
            this@HttpClientDefault.cancel()
        }
    }
}