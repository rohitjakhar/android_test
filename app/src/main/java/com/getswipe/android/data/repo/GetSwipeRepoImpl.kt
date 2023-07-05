package com.getswipe.android.data.repo

import com.getswipe.android.data.dto.toProductModel
import com.getswipe.android.data.webservice.GetSwipeService
import com.getswipe.android.domain.model.ProductModel
import com.getswipe.android.domain.repo.GetSwipeRepo
import com.getswipe.android.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    override suspend fun addProduct(productModel: ProductModel): Resource<String> =
        withContext(Dispatchers.IO) {
            try {
                val resp = GetSwipeService.getGetSwipeService().addProduct()
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
