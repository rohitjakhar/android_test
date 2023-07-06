package com.getswipe.android.domain.repo

import com.getswipe.android.domain.model.ProductModel
import com.getswipe.android.domain.model.ProductUploadModel
import com.getswipe.android.utils.Resource

interface GetSwipeRepo {
    suspend fun getProducts(): Resource<List<ProductModel>>
    suspend fun addProduct(productModel: ProductUploadModel): Resource<String>
}
