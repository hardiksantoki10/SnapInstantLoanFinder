package com.snap.instant.loan.finder.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.snap.instant.loan.finder.R
import com.snap.instant.loan.finder.apimodule.ApiRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var apiRepository: ApiRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        lifecycleScope.launch(Dispatchers.IO) {
            apiRepository.makeLogin("vijay@123", "vijaysinhvadher@gmail.com").onSuccess {
                Log.d("makeLogin", "responce ${it.string()}")
            }
        }

    }
}