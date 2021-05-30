package com.marcelo.beagleandroidtest.config

import br.com.zup.beagle.android.annotation.BeagleComponent
import br.com.zup.beagle.android.setup.DesignSystem
import com.marcelo.beagleandroidtest.R

@BeagleComponent
class AppDesignSystem : DesignSystem() {

    override fun textStyle(name: String): Int? {
        return when (name) {
            "H1" -> R.style.H1
            else -> R.style.Widget_AppCompat_TextView
        }
    }

}