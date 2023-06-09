package com.example.swipapplication.model.responses

import java.io.File

data class ProductItem(
    var image: String? = null,
    var price: Double? = null,
    var product_name: String? = null,
    var product_type: String? = null,
    var tax: Double? = null,
    var file: File? = null
)