package com.marcelo.beagleandroidtest

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import br.com.zup.beagle.android.annotation.BeagleComponent
import br.com.zup.beagle.android.annotation.RegisterController
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.ServerDrivenState
import kotlinx.android.synthetic.main.activity_server_driven.*

@RegisterController(id = "MAIN")
@BeagleComponent
class AppBeagleActivity: BeagleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_driven)

        refresh.setOnRefreshListener {
            refresh.isRefreshing = false
        }
    }

    override fun getServerDrivenContainerId(): Int = R.id.server_driven_container

    override fun getToolbar(): Toolbar = findViewById(R.id.toolbar)

    override fun onServerDrivenContainerStateChanged(state: ServerDrivenState) {

        when (state) {
            is ServerDrivenState.Started -> {
                progress_bar.visibility = View.VISIBLE
            }

            is ServerDrivenState.Finished -> {
                progress_bar.visibility = View.GONE
            }

        }

        if (state is ServerDrivenState.Error) {
            //Your error's treatment
            state.throwable.printStackTrace()
        }
    }
}