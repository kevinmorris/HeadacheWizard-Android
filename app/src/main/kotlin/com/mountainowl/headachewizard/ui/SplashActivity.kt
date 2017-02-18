package com.mountainowl.headachewizard.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.mountainowl.headachewizard.MainActivity
import com.mountainowl.headachewizard.model.DataManager

class SplashActivity : Activity() {

    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainIntent = Intent(this, MainActivity::class.java)
        Thread({
            val dataManager = DataManager.instance
            handler.post {
                startActivity(mainIntent)
                finish()
            }
        }).start()
    }
}
