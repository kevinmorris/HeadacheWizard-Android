package com.mountainowl.headachewizard

import android.app.Application
import android.content.Context
import com.mountainowl.headachewizard.model.DataManager

class HeadacheWizardApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        HeadacheWizardApplication.context = applicationContext
        DataManager.context = applicationContext
        DataManager.instance
    }

    companion object {

        lateinit var context: Context
            private set
    }

}
