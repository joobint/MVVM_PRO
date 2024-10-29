package com.app.machinetest.ui.product_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.machinetest.models.products.Product
import com.app.machinetest.models.products.ProductsApiResponse
import com.app.machinetest.utils.NetworkResult
import com.app.machinetest.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductListViewModel(val repository: AppRepository) :ViewModel() {

    var skip = 0
    var currentPage = 1
    var perPage = 10
    var isLockPaging = false
    var totalPage = 0
    var list: MutableList<Product?> = mutableListOf()

    private val _productsResponseMutableLiveDate = MutableLiveData<NetworkResult<ProductsApiResponse>>()
    val productsResponseLiveData: LiveData<NetworkResult<ProductsApiResponse>> = _productsResponseMutableLiveDate

    fun fetchProducts() = viewModelScope.launch(Dispatchers.IO){
        _productsResponseMutableLiveDate.postValue(NetworkResult.Loading())
        try {
            skip =  (currentPage - 1) * perPage
            val response = repository.fetchProducts(limit = perPage, skip = skip)
            if (response.isSuccessful){
                _productsResponseMutableLiveDate.postValue(NetworkResult.Success(response.body()!!))
            }else{
                _productsResponseMutableLiveDate.postValue( NetworkResult.Error(response.errorBody().toString()))
            }
        }catch (e: Exception){
            _productsResponseMutableLiveDate.postValue(NetworkResult.Error(e.message.toString()))
        }
    }
}