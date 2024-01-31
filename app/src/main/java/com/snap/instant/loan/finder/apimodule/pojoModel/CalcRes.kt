package com.snap.instant.loan.finder.apimodule.pojoModel


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class CalcRes(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("success")
    var success: Boolean?
) : Parcelable {
    @Parcelize
    data class Data(
        @SerializedName("monthly_payment")
        var monthlyPayment: Double?,
        @SerializedName("total_interest")
        var totalInterest: Double?,
        @SerializedName("total_payable")
        var totalPayable: Double?
    ) : Parcelable
}