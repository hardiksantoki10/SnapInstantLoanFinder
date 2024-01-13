package com.snap.instant.loan.finder.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.orhanobut.hawk.Hawk
import com.snap.instant.loan.finder.R
import com.snap.instant.loan.finder.activity.base.SAVED_TOKEN

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)



        Handler().postDelayed({

            if(Hawk.get(SAVED_TOKEN, "").equals("")){
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }else{
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }


        }, 2000)
    }
}