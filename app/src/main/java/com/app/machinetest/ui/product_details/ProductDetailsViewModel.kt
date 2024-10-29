package com.app.machinetest.ui.product_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.machinetest.models.product_details.ProductDetailsResponse
import com.app.machinetest.repository.AppRepository
import com.app.machinetest.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailsViewModel (val repository: AppRepository): ViewModel(){

    var productId:String? = null
    private val _productDetailsResponseMutableLiveDate = MutableLiveData<NetworkResult<ProductDetailsResponse>>()
    val productDetailsResponseLiveData: LiveData<NetworkResult<ProductDetailsResponse>> = _productDetailsResponseMutableLiveDate

    fun fetchProductDetails() = viewModelScope.launch(Dispatchers.IO){
        _productDetailsResponseMutableLiveDate.postValue(NetworkResult.Loading())
        try {
            if (productId == null) return@launch
            val response = repository.fetchProductDetails(id = productId!!)
            if (response.isSuccessful){
                _productDetailsResponseMutableLiveDate.postValue(NetworkResult.Success(response.body()!!))
            }else{
                _productDetailsResponseMutableLiveDate.postValue( NetworkResult.Error(response.errorBody().toString()))
            }
        }catch (e: Exception){
            _productDetailsResponseMutableLiveDate.postValue(NetworkResult.Error(e.message.toString()))
        }
    }

}