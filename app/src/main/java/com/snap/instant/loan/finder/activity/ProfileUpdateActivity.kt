package com.snap.instant.loan.finder.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.orhanobut.hawk.Hawk
import com.snap.instant.loan.finder.activity.base.BaseActivity
import com.snap.instant.loan.finder.activity.base.UserLoginDetail
import com.snap.instant.loan.finder.apimodule.ApiRepository
import com.snap.instant.loan.finder.apimodule.pojoModel.ProfileUpdateRes
import com.snap.instant.loan.finder.databinding.ActivityUpdateProfileBinding
import com.snap.instant.loan.finder.helper.BitmapUtils
import com.snap.instant.loan.finder.helper.EventModel
import com.snap.instant.loan.finder.helper.EventType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class ProfileUpdateActivity : BaseActivity() {

    private var selectedImageUri: Uri? = null

    @Inject
    lateinit var apiRepository: ApiRepository

    lateinit var binding: ActivityUpdateProfileBinding
    var isImageLoaded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setProfileData()
        binding.btnRegister.setOnClickListener {
            if (!isImageLoaded) {
                showToast(activity, "Please select image")
            } else if (isNotValidEditText(binding.edtFirstName)) {
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
            } else {
                showProgress()
                updateData()
            }
        }

        binding.ivEdit.setOnClickListener {
            openGallery()
        }

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    fun Context.getFilePathFromUri(uri: Uri): String? {
        var filePath: String? = null

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)

        cursor?.use {
            val columnIndex: Int = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            filePath = it.getString(columnIndex)
        }

        return filePath
    }

    private fun updateData() {
        if (selectedImageUri != null) {
            val filePath = getFilePathFromUri(selectedImageUri!!).orEmpty()
            if (filePath.isNotBlank()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val responseBody = apiRepository.updateProfile(
                        binding.edtFirstName.text.toString(),
                        binding.etLastName.text.toString(),
                        binding.edtEmail.text.toString(),
                        binding.edtNumber.text.toString(),
                        binding.edtDate.text.toString(),
                        filePath
                    )
                    responseBody.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            val res = response.body()?.string()
                            lifecycleScope.launch(
                                Dispatchers.Main
                            ) {
                                val profileUpdateRes =
                                    Gson().fromJson(res, ProfileUpdateRes::class.java)
                                hideProgress()
                                saveData(profileUpdateRes.data)
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            lifecycleScope.launch(
                                Dispatchers.Main
                            ) {
                                hideProgress()
                            }
                        }
                    })


                }
            } else {
                hideProgress()
            }
        } else if (Hawk.get(UserLoginDetail.IMAGE, "").orEmpty().isNotEmpty()) {
            var filePath = Hawk.get(UserLoginDetail.IMAGE, "").orEmpty()
            if (filePath.isNotBlank()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val bitmap =
                        Glide.with(this@ProfileUpdateActivity).asBitmap().load(filePath).submit()
                            .get();
                    BitmapUtils.saveBitmapToCache(
                        this@ProfileUpdateActivity,
                        bitmap,
                        "save_photo_${System.currentTimeMillis()}.jpg"
                    )?.let {
                        filePath = it
                    }
                    val responseBody = apiRepository.updateProfile(
                        binding.edtFirstName.text.toString(),
                        binding.etLastName.text.toString(),
                        binding.edtEmail.text.toString(),
                        binding.edtNumber.text.toString(),
                        binding.edtDate.text.toString(),
                        filePath
                    )

                    responseBody.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            val res = response.body()?.string()
                            lifecycleScope.launch(
                                Dispatchers.Main
                            ) {
                                val profileUpdateRes =
                                    Gson().fromJson(res, ProfileUpdateRes::class.java)
                                hideProgress()
                                saveData(profileUpdateRes.data)
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            lifecycleScope.launch(
                                Dispatchers.Main
                            ) {
                                hideProgress()
                            }
                        }
                    })
                }
            } else {
                hideProgress()
            }
        } else {
            hideProgress()
        }
    }

    private fun setProfileData() {
        binding.edtFirstName.setText(Hawk.get(UserLoginDetail.FIRST_NAME, "").orEmpty())
        binding.etLastName.setText(Hawk.get(UserLoginDetail.LAST_NAME, "").orEmpty())
        binding.edtDate.setText(Hawk.get(UserLoginDetail.DOB, "").orEmpty())
        binding.edtEmail.setText(Hawk.get(UserLoginDetail.EMAIL, "").orEmpty())
        binding.edtNumber.setText(Hawk.get(UserLoginDetail.CONTACT, "").orEmpty())
        Hawk.get(UserLoginDetail.IMAGE, "").orEmpty().let {
            if (it.isNotEmpty()) {
                isImageLoaded = true
                Glide.with(binding.ivImage).load(it).into(binding.ivImage)
            }
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImage.launch(galleryIntent)
    }


    val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Get the selected image URI
                selectedImageUri = result.data?.data

                // Set the selected image to the ImageView
                selectedImageUri?.let { uri ->
                    isImageLoaded = true
                    binding.ivImage.setImageURI(uri)
                }
            }
        }

    private fun saveData(loginRes: ProfileUpdateRes.Data?) {
        lifecycleScope.launch(Dispatchers.Main) {
            Hawk.put(UserLoginDetail.FIRST_NAME, loginRes?.firstName.orEmpty())
            Hawk.put(UserLoginDetail.LAST_NAME, loginRes?.lastName.orEmpty())
            Hawk.put(UserLoginDetail.DOB, loginRes?.dob.orEmpty())
            Hawk.put(UserLoginDetail.EMAIL, loginRes?.email.orEmpty())
            Hawk.put(UserLoginDetail.CONTACT, loginRes?.contact.orEmpty())
            Hawk.put(UserLoginDetail.IMAGE, loginRes?.fullImage.orEmpty())
            EventBus.getDefault().post(EventModel(EventType.PROFILE_UPDATE, null, true))
            finish()
        }
    }
}