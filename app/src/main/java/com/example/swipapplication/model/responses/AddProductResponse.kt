package com.example.swipapplication.model.responses

data class AddProductResponse(
    val message: String,
    val product_details: ProductItem,
    val product_id: Int,
    val success: Boolean
)