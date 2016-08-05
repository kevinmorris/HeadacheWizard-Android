package com.mountainowl.headachewizard

import android.app.Application
import android.content.Context

class HeadacheWizardApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        HeadacheWizardApplication.context = applicationContext
    }

    companion object {

        var context: Context? = null
            private set
    }

}
