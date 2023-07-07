package com.getswipe.android.domain.model

data class ProductModel(
    val image: String?,
    val price: String,
    val productName: String,
    val productType: String,
    val tax: String,
) {
    fun search(key: String): Boolean {
        if (productName.contains(key, true)) {
            return true
        }
        if (productType.contains(key, true)) {
            return true
        }
        if (price.contains(key, true)) {
            return true
        }
        return false
    }
}
