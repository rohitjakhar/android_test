package com.getswipe.android.domain.model

import java.io.File

data class ProductUploadModel(
    val image: File?,
    val price: String,
    val productName: String,
    val productType: String,
    val tax: String,
)
