package com.snap.instant.loan.finder.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.snap.instant.loan.finder.R
import com.snap.instant.loan.finder.apimodule.ApiRepository
import com.snap.instant.loan.finder.databinding.ActivityLoginBinding
import com.snap.instant.loan.finder.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding


    @Inject
    lateinit var apiRepository: ApiRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.txtSignup.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }


        lifecycleScope.launch(Dispatchers.IO) {
            apiRepository.makeLogin("vijay@123", "vijaysinhvadher@gmail.com").onSuccess {
                Log.d("makeLogin", "responce ${it.string()}")
            }
        }

    }
}