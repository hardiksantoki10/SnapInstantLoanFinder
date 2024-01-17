package com.snap.instant.loan.finder.apimodule

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
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


    @Multipart
    @POST("api/applications-user-login")
    suspend fun makeLogin(
        @PartMap map: HashMap<String, RequestBody>,
        @Part g_photo: MultipartBody.Part
    ): Result<ResponseBody>

    @Multipart
    @POST("api/applications-upload-user-contents")
    fun uploadUserContent(
        @PartMap map: HashMap<String, RequestBody>,
        @Part preview: MultipartBody.Part,
        @Part zip: MultipartBody.Part
    ): Call<ResponseBody>

}


