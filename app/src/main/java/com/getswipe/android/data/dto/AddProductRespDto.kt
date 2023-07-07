package com.getswipe.android.data.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AddProductRespDto(
    @SerializedName("message")
    val message: String?,
    @SerializedName("product_id")
    val productId: Int?,
    @SerializedName("success")
    val success: Boolean?,
)
