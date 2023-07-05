package com.getswipe.android.domain.model

data class ProductModel(
    val image: String?,
    val price: Long,
    val productName: String,
    val productType: String,
    val tax: Long,
)
