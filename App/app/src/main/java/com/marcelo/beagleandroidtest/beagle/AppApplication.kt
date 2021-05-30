package com.marcelo.beagleandroidtest.beagle

import androidx.multidex.MultiDexApplication
import com.marcelo.beagleandroidtest.BeagleSetup

class AppApplication: MultiDexApplication() {

//    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(newBase)
//        MultiDex.install(this)
//    }

    override fun onCreate() {
        super.onCreate()
        BeagleSetup().init(this)
    }
}