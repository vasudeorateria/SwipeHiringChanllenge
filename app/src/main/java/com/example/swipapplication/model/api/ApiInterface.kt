package com.example.swipapplication.model.api

import com.example.swipapplication.BuildConfig
import com.example.swipapplication.model.responses.AddProductResponse
import com.example.swipapplication.model.responses.ProductItem
import okhttp3.ConnectionPool
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.concurrent.TimeUnit

interface ApiInterface {

    companion object{
        operator fun invoke(): ApiInterface {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val okHttpInterceptor = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))
                .retryOnConnectionFailure(true)
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpInterceptor)
                .build()
                .create(ApiInterface::class.java)
        }
    }

    @GET("public/get")
    suspend fun getProducts(): ArrayList<ProductItem>

    @Multipart
    @POST("public/add")
    suspend fun addProduct(@Part addProductItem: List<MultipartBody.Part>): AddProductResponse

}