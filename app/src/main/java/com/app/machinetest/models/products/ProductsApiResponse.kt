package com.app.machinetest.models.products


import com.google.gson.annotations.SerializedName

data class ProductsApiResponse(
    @SerializedName("limit")
    val limit: Int?,
    @SerializedName("products")
    val products: List<Product?>?,
    @SerializedName("skip")
    val skip: Int?,
    @SerializedName("total")
    val total: Int?
)