package com.example.swipapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swipapplication.model.Repository
import com.example.swipapplication.model.api.ResourceStatus
import com.example.swipapplication.model.responses.ProductItem
import kotlinx.coroutines.launch

class ProductsFragmentViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _productList = MutableLiveData<List<ProductItem>?>(null)
    val productList = MutableLiveData<List<ProductItem>?>(null)
    var isLoading = MutableLiveData(true)

    init {
        getProducts()
    }

    fun refreshList() {
        if (repository.productAdded) {
            getProducts()
            repository.productAdded = false
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            repository.getProducts().collect {
                when (it.status) {
                    ResourceStatus.SUCCESS -> {
                        isLoading.postValue(false)
                    }
                    ResourceStatus.ERROR -> {
                        isLoading.postValue(false)
                    }
                    ResourceStatus.LOADING -> {
                        isLoading.postValue(true)
                    }
                }
                _productList.postValue(it.data)
                productList.postValue(it.data)
            }
        }
    }

    fun searchProducts(query: String) {
        if (isLoading.value != false) return
        if (_productList.value.isNullOrEmpty()) return
        val filteredList = _productList.value!!.filter {
            it.product_name!!.lowercase().contains(query) ||
                    it.product_type!!.lowercase().contains(query) ||
                    it.price.toString().lowercase().contains(query) ||
                    it.tax.toString().lowercase().contains(query)
        }
        productList.postValue(filteredList)
    }

}