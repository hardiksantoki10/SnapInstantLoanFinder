package com.snap.instant.loan.finder.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.compose.ui.text.toLowerCase
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.snap.instant.loan.finder.R
import com.snap.instant.loan.finder.apimodule.ApiRepository
import com.snap.instant.loan.finder.databinding.ActivityRegisterBinding
import com.snap.instant.loan.finder.helper.BaseActivity
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


        binding.btnRegister.setOnClickListener {

            if(isNotValidEditText(binding.edtFirstName)){
                showToast(activity,"Please enter firstname")
            }else if(isNotValidEditText(binding.etLastName)){
                showToast(activity,"Please enter lastname")
            }else if(isNotValidEditText(binding.edtEmail)){
                showToast(activity,"Please enter email")
            }else if(!isValidEmail(binding.edtEmail.text.toString())){
                showToast(activity,"Please enter valid email")
            }else if(isNotValidEditText(binding.edtNumber)){
                showToast(activity,"Please enter number")
            }else if(isNotValidEditText(binding.edtDate)){
                showToast(activity,"Please enter date")
            }else if(isNotValidEditText(binding.edtNewPassword)){
                showToast(activity,"Please enter password")
            }else if(isNotValidEditText(binding.etConfirmPassword)){
                showToast(activity,"Please enter confirm password")
            }else if(!binding.edtNewPassword.text.toString().lowercase(Locale.getDefault()).equals(binding.etConfirmPassword.text.toString()
                    .lowercase(Locale.getDefault()))){
                showToast(activity,"Password is not matched")
            }else{
                showProgress()
                lifecycleScope.launch(Dispatchers.IO) {
                    apiRepository.makeRegister(binding.edtFirstName.text.toString(),
                        binding.etLastName.text.toString(),
                        binding.edtEmail.text.toString(),
                        binding.edtNumber.text.toString(),
                        binding.edtDate.text.toString(),
                        binding.edtNewPassword.text.toString(),
                        binding.etConfirmPassword.text.toString()).onSuccess {

                        hideProgress()
                        Log.d("makeRegister", "responce ${it.string()}")
                    }.onFailure {

                        hideProgress()
                        Log.d("makeLogin", "responce $it")
                    }
                }
            }


        }

    }
}