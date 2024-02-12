package com.snap.instant.loan.finder.apimodule.pojoModel


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class CompanyProfileRes(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("success")
    var success: Boolean?
) : Parcelable {
    @Parcelize
    data class Data(
        @SerializedName("average_terms")
        var averageTerms: String?,
        @SerializedName("benifits")
        var benifits: List<String?>?,
        @SerializedName("companyName")
        var companyName: String?,
        @SerializedName("credit_requirements")
        var creditRequirements: String?,
        @SerializedName("details")
        var details: String?,
        @SerializedName("full_image")
        var fullImage: String?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("image")
        var image: String?,
        @SerializedName("interest_rates_average")
        var interestRatesAverage: String?,
        @SerializedName("redirect_link")
        var redirectLink: String?,
        @SerializedName("resources")
        var resources: List<Resource?>?,
        @SerializedName("subtitle")
        var subtitle: String?
    ) : Parcelable {
        @Parcelize
        data class Resource(
            @SerializedName("details")
            var details: String?,
            @SerializedName("id")
            var id: String?,
            @SerializedName("title")
            var title: String?,
            @SerializedName("full_image")
            var full_image: String?
        ) : Parcelable
    }
}