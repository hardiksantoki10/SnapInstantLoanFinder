package com.snap.instant.loan.finder.apimodule.pojoModel


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ProfileRes(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("success")
    var success: Boolean?
) : Parcelable {
    @Parcelize
    data class Data(
        @SerializedName("app_type")
        var appType: String?,
        @SerializedName("contact")
        var contact: String?,
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("device_token")
        var deviceToken: String?,
        @SerializedName("dob")
        var dob: String?,
        @SerializedName("email")
        var email: String?,
        @SerializedName("first_name")
        var firstName: String?,
        @SerializedName("full_image")
        var fullImage: String?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("image")
        var image: String?,
        @SerializedName("last_name")
        var lastName: String?,
     /*   @SerializedName("otp")
        var otp: Any?,*/
        @SerializedName("password")
        var password: String?,
        @SerializedName("remember_token")
        var rememberToken: String?,
        @SerializedName("updated_at")
        var updatedAt: String?
    ) : Parcelable
}