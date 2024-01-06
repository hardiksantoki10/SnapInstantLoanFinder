package com.snap.instant.loan.finder.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.snap.instant.loan.finder.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({

            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }, 2000)
    }
}