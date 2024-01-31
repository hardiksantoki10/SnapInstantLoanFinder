package com.snap.instant.loan.finder.apimodule

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.util.HashMap

interface Api {

    @FormUrlEncoded
    @POST("{path}")
    suspend fun getData(
        @Path("path") path: String, @FieldMap hashMap: HashMap<String, String>
    ): Result<ResponseBody>

    @GET("home")
    suspend fun getHomeData(): Result<ResponseBody>

    @GET("category")
    suspend fun getCategoryData(): Result<ResponseBody>

    @GET("get-profile")
    suspend fun getProfile(): Result<ResponseBody>

    @POST("company-profile")
    suspend fun getCompanyProfile(@Body map: RequestBody): Result<ResponseBody>
    @POST("loan-calculater")
    suspend fun loanCalculate(@Body map: RequestBody): Result<ResponseBody>


    @POST("register")
    suspend fun register(
        @Body map: RequestBody
    ): Result<ResponseBody>

    @Multipart
    @POST("profile-update")
    fun updateProfile(
        @PartMap map: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part,
    ): Call<ResponseBody>


}


