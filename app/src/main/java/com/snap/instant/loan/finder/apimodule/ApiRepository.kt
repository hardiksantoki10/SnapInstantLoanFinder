package com.snap.instant.loan.finder.apimodule

import com.snap.instant.loan.finder.apimodule.RequestBuilder.toRequestBody
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
open class ApiRepository @Inject constructor(private val api: Api) {

    open suspend fun makeLogin(password: String, email: String): Result<ResponseBody> {
        return api.getData(RequestBuilder.ApiEndPoints.LOGIN,
            RequestBuilder.getCommonParametersHashMap().apply {
                put("password", password)
                put("email", email)
            })
    }

    open suspend fun getHomeData(): Result<ResponseBody> {
        return api.getHomeData()
    }

    open suspend fun getCategory(): Result<ResponseBody> {
        return api.getCategoryData()
    }

    open suspend fun getProfile(): Result<ResponseBody> {
        return api.getProfile()
    }

    open suspend fun deleteProfile(): Result<ResponseBody> {
        return api.deleteProfile()
    }

    open suspend fun getCompanyProfile(company_id: String): Result<ResponseBody> {
        return api.getCompanyProfile(
            MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("company_id", company_id)
                .build()
        )
    }

    open suspend fun companylistByCategory(company_id: String): Result<ResponseBody> {
        return api.companylistByCategory(
            MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("category_id", company_id)
                .build()
        )
    }

    open suspend fun loanCalculate(loan_amount: String,interest_rate: String,loan_years: String): Result<ResponseBody> {
        return api.loanCalculate(
            MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("loan_amount", loan_amount)
                .addFormDataPart("interest_rate", interest_rate)
                .addFormDataPart("loan_years", loan_years)
                .build()
        )
    }

    open suspend fun makeRegister(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        dob: String,
        password: String,
        confirmPassword: String
    ): Result<ResponseBody> {
        return api.register(
            /*  RequestBuilder.getCommonParametersHashMapRequestBody().apply {
                  put("first_name", toRequestBody(firstName))
                  put("last_name",toRequestBody( lastName))
                  put("email", toRequestBody(email))
                  put("password",toRequestBody( password))
                  put("confirm_password", toRequestBody(confirmPassword))
                  put("contact", toRequestBody(phoneNumber))
                  put("dob", toRequestBody(dob))
                  put("device_token", toRequestBody(System.currentTimeMillis().toString()))
                  put("app_type", toRequestBody("0"))

              }*/
            MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("first_name", firstName)
                .addFormDataPart("last_name", lastName)
                .addFormDataPart("email", email)
                .addFormDataPart("password", password)
                .addFormDataPart("confirm_password", confirmPassword)
                .addFormDataPart("contact", phoneNumber)
                .addFormDataPart("dob", dob)
                .addFormDataPart("device_token", System.currentTimeMillis().toString())
                .addFormDataPart("app_type", "0")
                .build()
        )
    }

    open suspend fun updateProfile(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        dob: String,
        filePath: String,
    ): Call<ResponseBody>
    {
        return api.updateProfile(
            RequestBuilder.getCommonParametersHashMapRequestBody().apply {
                put("first_name", toRequestBody(firstName))
                put("last_name", toRequestBody(lastName))
                put("email", toRequestBody(email))
                put("contact", toRequestBody(phoneNumber))
                put("dob", toRequestBody(dob))
            }, RequestBuilder.getRequestBodyForFile(File(filePath), "image")

        )
    }
}