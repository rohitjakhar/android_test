package com.getswipe.android.data.dto

import com.getswipe.android.domain.model.ProductModel
import com.google.gson.annotations.SerializedName

data class ProductsItemDto(
    @SerializedName("image")
    val image: String?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("product_name")
    val productName: String?,
    @SerializedName("product_type")
    val productType: String?,
    @SerializedName("tax")
    val tax: Double?,
)

fun ProductsItemDto.toProductModel(): ProductModel {
    return ProductModel(
        image = image,
        price = price ?: 0.0,
        productName = productName ?: "",
        productType = productType ?: "",
        tax = tax ?: 0.0,
    )
}
