package com.getswipe.android.data.repo

import com.getswipe.android.data.dto.toProductModel
import com.getswipe.android.data.webservice.GetSwipeService
import com.getswipe.android.domain.model.ProductModel
import com.getswipe.android.domain.model.ProductUploadModel
import com.getswipe.android.domain.repo.GetSwipeRepo
import com.getswipe.android.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class GetSwipeRepoImpl : GetSwipeRepo {
    override suspend fun getProducts(): Resource<List<ProductModel>> = withContext(Dispatchers.IO) {
        try {
            val resp = GetSwipeService.getGetSwipeService().getProductList()
            return@withContext if (resp.isSuccessful && !resp.body().isNullOrEmpty()) {
                Resource.SUCCESS(data = resp.body()!!.map { it.toProductModel() })
            } else {
                Resource.FAILURE(
                    if (resp.body().isNullOrEmpty()) "Data is Null" else resp.message(),
                )
            }
        } catch (e: Exception) {
            return@withContext Resource.FAILURE(e.message)
        }
    }

    override suspend fun addProduct(productModel: ProductUploadModel): Resource<String> =
        withContext(Dispatchers.IO) {
            try {
                val imagePart = productModel.image?.let {
                    MultipartBody.Part.createFormData(
                        "files[]",
                        it.name,
                        it.asRequestBody("image/*".toMediaType()),
                    )
                }
                val resp = GetSwipeService.getGetSwipeService().addProduct(
                    productType = productModel.productType.toRequestBody("text/plain".toMediaType()),
                    productName = productModel.productName.toRequestBody("text/plain".toMediaType()),
                    productPrice = productModel.price.toRequestBody("text/plain".toMediaType()),
                    productTax = productModel.tax.toRequestBody("text/plain".toMediaType()),
                    productImage = imagePart,
                )
                return@withContext if (resp.isSuccessful && resp.body()?.success == true) {
                    Resource.SUCCESS(resp.body()?.productId.toString())
                } else {
                    Resource.FAILURE(resp.message())
                }
            } catch (e: Exception) {
                return@withContext Resource.FAILURE(e.message)
            }
        }
}
