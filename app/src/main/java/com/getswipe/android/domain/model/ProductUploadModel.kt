package com.getswipe.android.domain.model

import androidx.annotation.Keep
import java.io.File
@Keep
data class ProductUploadModel(
    val image: File?,
    val price: String,
    val productName: String,
    val productType: String,
    val tax: String,
)
