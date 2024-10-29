package com.app.machinetest.ui.product_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.machinetest.network.RetrofitInstance
import com.app.machinetest.repository.AppRepository

@Suppress("UNCHECKED_CAST")
class ProductListViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = AppRepository(RetrofitInstance.getInstance())
        return if (modelClass.isAssignableFrom(ProductListViewModel::class.java)) {
            ProductListViewModel(repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}