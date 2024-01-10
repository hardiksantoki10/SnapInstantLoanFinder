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

}