package com.snap.instant.loan.finder.apimodule.pojoModel


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class LoginRes(
    @SerializedName("message")
    var message: String?,
    @SerializedName("success")
    var success: Boolean?,
    @SerializedName("contact")
    var contact: String?,
    @SerializedName("dob")
    var dob: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("first_name")
    var firstName: String?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("is_verified")
    var isVerified: String?,
    @SerializedName("last_name")
    var lastName: String?,
    @SerializedName("password")
    var password: String?,
    @SerializedName("remember_token")
    var rememberToken: String?
) : Parcelable