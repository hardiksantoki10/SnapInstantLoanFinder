package com.snap.instant.loan.finder.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.orhanobut.hawk.Hawk
import com.snap.instant.loan.finder.R
import com.snap.instant.loan.finder.activity.base.UserLoginDetail
import com.snap.instant.loan.finder.apimodule.ApiRepository
import com.snap.instant.loan.finder.apimodule.pojoModel.LoginRes
import com.snap.instant.loan.finder.databinding.ActivityRegisterBinding
import com.snap.instant.loan.finder.activity.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : BaseActivity() {

    @Inject
    lateinit var apiRepository: ApiRepository

    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.btnRegister.setOnClickListener {
            if (isNotValidEditText(binding.edtFirstName)) {
                showToast(activity, "Please enter firstname")
            } else if (isNotValidEditText(binding.etLastName)) {
                showToast(activity, "Please enter lastname")
            } else if (isNotValidEditText(binding.edtEmail)) {
                showToast(activity, "Please enter email")
            } else if (!isValidEmail(binding.edtEmail.text.toString())) {
                showToast(activity, "Please enter valid email")
            } else if (isNotValidEditText(binding.edtNumber)) {
                showToast(activity, "Please enter number")
            } else if (isNotValidEditText(binding.edtDate)) {
                showToast(activity, "Please enter date")
            } else if (isNotValidEditText(binding.edtNewPassword)) {
                showToast(activity, "Please enter password")
            } else if (isNotValidEditText(binding.etConfirmPassword)) {
                showToast(activity, "Please enter confirm password")
            } else if (!binding.edtNewPassword.text.toString().lowercase(Locale.getDefault())
                    .equals(
                        binding.etConfirmPassword.text.toString()
                            .lowercase(Locale.getDefault())
                    )
            ) {
                showToast(activity, "Password is not matched")
            } else {
                showProgress()
                lifecycleScope.launch(Dispatchers.IO) {
                    apiRepository.makeRegister(
                        binding.edtFirstName.text.toString(),
                        binding.etLastName.text.toString(),
                        binding.edtEmail.text.toString(),
                        binding.edtNumber.text.toString(),
                        binding.edtDate.text.toString(),
                        binding.edtNewPassword.text.toString(),
                        binding.etConfirmPassword.text.toString()
                    ).onSuccess {
                        lifecycleScope.launch(Dispatchers.Main) {
                            hideProgress()
                            val res = it.string()
                            Log.d("makeLogin", "responce $res")
                            val loginRes = Gson().fromJson(res, LoginRes::class.java)
                            if (loginRes.success == true) {
                                saveData(loginRes)
                            } else {

                            }
                        }
                    }.onFailure {
                        lifecycleScope.launch(Dispatchers.Main) {
                            hideProgress()
                            Log.d("makeLogin", "responce $it")
                        }
                    }
                }
            }
        }
    }

    private fun saveData(loginRes: LoginRes?) {
        lifecycleScope.launch(Dispatchers.Main) {
            Hawk.put(UserLoginDetail.FIRST_NAME, loginRes?.firstName.orEmpty())
            Hawk.put(UserLoginDetail.FIRST_NAME, loginRes?.firstName.orEmpty())
            Hawk.put(UserLoginDetail.ID, loginRes?.id.orEmpty())
            Hawk.put(UserLoginDetail.LAST_NAME, loginRes?.lastName.orEmpty())
            Hawk.put(UserLoginDetail.DOB, loginRes?.dob.orEmpty())
            Hawk.put(UserLoginDetail.EMAIL, loginRes?.email.orEmpty())
            Hawk.put(UserLoginDetail.PASSWORD, loginRes?.password.orEmpty())
            Hawk.put(UserLoginDetail.CONTACT, loginRes?.contact.orEmpty())
            Hawk.put(UserLoginDetail.IS_VERIFIED, loginRes?.isVerified.orEmpty())
            Hawk.put(UserLoginDetail.REMEMBER_TOKEN, loginRes?.rememberToken.orEmpty())
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
}