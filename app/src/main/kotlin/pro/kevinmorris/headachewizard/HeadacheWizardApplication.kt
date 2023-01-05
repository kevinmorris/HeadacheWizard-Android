package pro.kevinmorris.headachewizard

import android.app.Application
import android.content.Context
import pro.kevinmorris.headachewizard.model.DataManager

class HeadacheWizardApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        HeadacheWizardApplication.context = applicationContext
        DataManager.context = applicationContext
    }

    companion object {

        lateinit var context: Context
            private set
    }

}
