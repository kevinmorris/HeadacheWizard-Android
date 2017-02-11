package com.mountainowl.headachewizard.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.mountainowl.headachewizard.MainActivity

class SplashActivity : Activity() {

    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainIntent = Intent(this, MainActivity::class.java)
        handler.postDelayed({
            startActivity(mainIntent)
            finish()
        }, 1000)
    }
}
