package com.marcelo.beagleandroidtest

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.utils.newServerDrivenIntent
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.ScreenRequest
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URI

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener {
//            startActivity(BeagleActivity.newIntent(this, ScreenRequest("/screen")))

//            val intent = this.newServerDrivenIntent<AppBeagleActivity>(RequestData(uri = URI.create("/screen")))
//            startActivity(intent)

//            startActivity(newServerDrivenIntent<AppBeagleActivity>(RequestData(URI("/screen"))))

            startActivity(BeagleActivity.newIntent(this, ScreenRequest("/screen")))
        }


//        finish()

    }

}