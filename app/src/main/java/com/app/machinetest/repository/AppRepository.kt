package com.app.machinetest.repository

import com.app.machinetest.network.ApiService

class AppRepository(private val retrofitService: ApiService) {

    suspend fun fetchProducts(
        limit: Int,
        skip: Int
    ) = retrofitService.fetchProducts(limit, skip)

    suspend fun fetchProductDetails(
      id:String
    ) = retrofitService.fetchProductDetails(id)
}