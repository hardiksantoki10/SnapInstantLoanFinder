package com.snap.instant.loan.finder.apimodule.pojoModel


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class LoanProviderRes(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("success")
    var success: Boolean?
) : Parcelable {
    @Parcelize
    data class Data(
        @SerializedName("id")
        var id: String?,
        @SerializedName("image")
        var image: String?,
        @SerializedName("name")
        var name: String?,
        @SerializedName("subtitle")
        var subtitle: String?
    ) : Parcelable
}