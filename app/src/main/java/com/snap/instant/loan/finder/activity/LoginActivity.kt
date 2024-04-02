package com.snap.instant.loan.finder.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.orhanobut.hawk.Hawk
import com.snap.instant.loan.finder.R
import com.snap.instant.loan.finder.activity.base.UserLoginDetail.CONTACT
import com.snap.instant.loan.finder.activity.base.UserLoginDetail.DOB
import com.snap.instant.loan.finder.activity.base.UserLoginDetail.EMAIL
import com.snap.instant.loan.finder.activity.base.UserLoginDetail.FIRST_NAME
import com.snap.instant.loan.finder.activity.base.UserLoginDetail.ID
import com.snap.instant.loan.finder.activity.base.UserLoginDetail.IS_VERIFIED
import com.snap.instant.loan.finder.activity.base.UserLoginDetail.LAST_NAME
import com.snap.instant.loan.finder.activity.base.UserLoginDetail.PASSWORD
import com.snap.instant.loan.finder.activity.base.UserLoginDetail.REMEMBER_TOKEN
import com.snap.instant.loan.finder.apimodule.ApiRepository
import com.snap.instant.loan.finder.apimodule.pojoModel.LoginRes
import com.snap.instant.loan.finder.databinding.ActivityLoginBinding
import com.snap.instant.loan.finder.activity.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

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

        binding.btnLogin.setOnClickListener {
            if (isNotValidEditText(binding.etEmail)) {
                showToast(activity, "Please enter email")
            } else if (!isValidEmail(binding.etEmail.text.toString())) {
                showToast(activity, "Please enter valid email")
            } else if (isNotValidEditText(binding.etPassword)) {
                showToast(activity, "Please enter password")
            } else {
                showProgress()
                lifecycleScope.launch(Dispatchers.IO) {
                    apiRepository.makeLogin(
                        binding.etPassword.text.toString(),
                        binding.etEmail.text.toString()
                    ).onSuccess {
                        lifecycleScope.launch(Dispatchers.Main) {
                            hideProgress()
                            val res = it.string()
                            Log.d("makeLogin", "responce $res")
                            val loginRes = Gson().fromJson(res, LoginRes::class.java)
                            if (loginRes.success == true) {
                                saveData(loginRes)
                            } else {
                                showToast(activity, "${loginRes.message}")
                            }
                        }
                    }.onFailure {
                        lifecycleScope.launch(Dispatchers.Main) {
                            hideProgress()
                        }
                    }
                }
            }
        }
    }

    private fun saveData(loginRes: LoginRes?) {
        lifecycleScope.launch(Dispatchers.Main) {
            Hawk.put(FIRST_NAME, loginRes?.firstName.orEmpty())
            Hawk.put(FIRST_NAME, loginRes?.firstName.orEmpty())
            Hawk.put(ID, loginRes?.id.orEmpty())
            Hawk.put(LAST_NAME, loginRes?.lastName.orEmpty())
            Hawk.put(DOB, loginRes?.dob.orEmpty())
            Hawk.put(EMAIL, loginRes?.email.orEmpty())
            Hawk.put(PASSWORD, loginRes?.password.orEmpty())
            Hawk.put(CONTACT, loginRes?.contact.orEmpty())
            Hawk.put(IS_VERIFIED, loginRes?.isVerified.orEmpty())
            Hawk.put(REMEMBER_TOKEN, loginRes?.rememberToken.orEmpty())
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
}