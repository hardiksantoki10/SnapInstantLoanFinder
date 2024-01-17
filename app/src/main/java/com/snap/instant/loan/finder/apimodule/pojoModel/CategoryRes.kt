package com.snap.instant.loan.finder.apimodule.pojoModel

data class CategoryRes(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val companies: String,
        val created_at: String,
        val details: String,
        val icon_image: String,
        val id: String,
        val interest_rate: String,
        val title: String,
        val updated_at: String
    )
}