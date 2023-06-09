package com.example.swipapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swipapplication.model.Repository
import com.example.swipapplication.model.api.ResourceStatus
import com.example.swipapplication.model.responses.ProductItem
import kotlinx.coroutines.launch
import java.io.File


class AddProductsFragmentViewModel(
    private val repository: Repository
) : ViewModel() {

    val isProductDetailsValid = MutableLiveData(false)
    val isLoading = MutableLiveData(false)
    val isAdded = MutableLiveData(false)
    val product = MutableLiveData<ProductItem?>(null)
    val productTypeList = listOf("type 1", "type 2", "type 3", "type 4")

    fun isProductDetailsValid(
        product_name: String,
        product_type: String,
        price: String,
        tax: String,
        file: File? = null
    ) {
        val productItem = product.value ?: ProductItem()
        productItem.product_name = product_name
        productItem.product_type = product_type
        if (price.isEmpty()) {
            productItem.price = null
        } else {
            productItem.price = price.toDouble()
        }
        if (tax.isEmpty()) {
            productItem.tax = null
        } else {
            productItem.tax = tax.toDouble()
        }
        productItem.file = file
        product.postValue(productItem)
        isProductDetailsValid.postValue(
            product_name.isNotEmpty() &&
                    product_type.isNotEmpty() && productTypeList.contains(product_type) &&
                    price.isNotEmpty() && price.contains(".") && price.last() != '.' &&
                    tax.isNotEmpty() && tax.contains(".") && tax.last() != '.'
        )
    }

    fun addProduct() {
        if (product.value == null) return
        viewModelScope.launch {
            repository.addProduct(product.value!!).collect() {
                when (it.status) {
                    ResourceStatus.SUCCESS -> {
                        isLoading.postValue(false)
                        isAdded.postValue(true)
                    }
                    ResourceStatus.ERROR -> {
                        isLoading.postValue(false)
                        product.postValue(null)
                        isAdded.postValue(false)
                    }
                    ResourceStatus.LOADING -> {
                        isLoading.postValue(true)
                    }
                }
            }
        }
    }

    fun productAdded() {
        product.postValue(null)
        isAdded.postValue(false)
        isProductDetailsValid.postValue(false)
    }
}