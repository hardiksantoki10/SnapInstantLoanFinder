package com.snap.instant.loan.finder.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.orhanobut.hawk.Hawk
import com.snap.instant.loan.finder.R
import com.snap.instant.loan.finder.activity.base.UserLoginDetail

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            if (Hawk.get(UserLoginDetail.REMEMBER_TOKEN, "").isNullOrBlank()) {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
            } else {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }, 2000)
    }
}