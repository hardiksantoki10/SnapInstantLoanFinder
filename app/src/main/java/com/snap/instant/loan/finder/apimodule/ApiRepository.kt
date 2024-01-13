package com.snap.instant.loan.finder.apimodule

import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
open class ApiRepository @Inject constructor(private val api: Api) {

    open suspend fun makeLogin(password: String, email: String): Result<ResponseBody> {
        return api.getData(
            RequestBuilder.ApiEndPoints.LOGIN,
            RequestBuilder.getCommonParametersHashMap().apply {
                put("password", password)
                put("email", email)
            }
        )
    }

    open suspend fun makeRegister(firstName: String,
                                  lastName: String,
                                  email: String,
                                  phoneNumber: String,
                                  dob: String,
                                  password: String,
                                  confirmPassword: String): Result<ResponseBody> {
        return api.getData(
            RequestBuilder.ApiEndPoints.REGISTER,
            RequestBuilder.getCommonParametersHashMap().apply {
                put("first_name", firstName)
                put("last_name", lastName)
                put("email", email)
                put("password", password)
                put("confirm_password", confirmPassword)
                put("contact", phoneNumber)
                put("dob", dob)
                put("device_token", "123456")
                put("app_type", "0")
            }
        )
    }
}