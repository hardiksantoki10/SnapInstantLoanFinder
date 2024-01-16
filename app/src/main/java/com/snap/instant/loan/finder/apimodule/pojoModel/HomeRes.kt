package com.snap.instant.loan.finder.apimodule.pojoModel


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class HomeRes(
    @SerializedName("companies")
    var companies: List<Company?>?,
    @SerializedName("home")
    var home: List<Home?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("success")
    var success: Boolean?
) : Parcelable {
    @Parcelize
    data class Company(
        @SerializedName("id")
        var id: String?,
        @SerializedName("image")
        var image: String?,
        @SerializedName("name")
        var name: String?,
        @SerializedName("subtitle")
        var subtitle: String?
    ) : Parcelable

    @Parcelize
    data class Home(
        @SerializedName("description")
        var description: String?,
        @SerializedName("image")
        var image: String?,
        @SerializedName("title")
        var title: String?
    ) : Parcelable
}