package com.app.machinetest.network

import com.app.machinetest.models.product_details.ProductDetailsResponse
import com.app.machinetest.models.products.ProductsApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("products?")
    suspend fun fetchProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): Response<ProductsApiResponse>

    @GET("products/{id}")
    suspend fun fetchProductDetails(
        @Path("id") id: String
    ): Response<ProductDetailsResponse>

}