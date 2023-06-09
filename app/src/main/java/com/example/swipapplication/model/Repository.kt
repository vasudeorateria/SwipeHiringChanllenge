package com.example.swipapplication.model

import android.util.Log
import com.example.swipapplication.model.api.ApiInterface
import com.example.swipapplication.model.api.Resource
import com.example.swipapplication.model.responses.AddProductResponse
import com.example.swipapplication.model.responses.ProductItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class Repository(
   private val apiInterface: ApiInterface
) {


    var productAdded = false

    suspend fun getProducts(): Flow<Resource<List<ProductItem>>> {
        return flow {
            emit(Resource.Loading(null))
            try {
                emit(Resource.Success(apiInterface.getProducts()))
            } catch (exception: Exception) {
                Log.e("getProducts", exception.message.toString())
                emit(Resource.Error(null, exception))
            }
        }
    }

    suspend fun addProduct(productItem: ProductItem): Flow<Resource<AddProductResponse>> {
        return flow {
            emit(Resource.Loading(null))
            try {
                val body = MultipartBody.Builder()
                    .addFormDataPart("product_name", productItem.product_name!!)
                    .addFormDataPart("product_type", productItem.product_type!!)
                    .addFormDataPart("price", productItem.price.toString())
                    .addFormDataPart("tax", productItem.tax.toString())
                if (productItem.file != null) {
                    body.addFormDataPart(
                        "files[]",
                        productItem.file!!.name,
                        productItem.file!!.asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }
                productAdded = true
                emit(Resource.Success(apiInterface.addProduct(body.build().parts)))
            } catch (exception: Exception) {
                Log.e("addProduct", exception.message.toString())
                emit(Resource.Error(null, exception))
            }
        }
    }
}