package com.getswipe.android.data.webservice

import com.getswipe.android.data.dto.AddProductRespDto
import com.getswipe.android.data.dto.ProductsItemDto
import com.getswipe.android.utils.Constant
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface GetSwipeService {
    @GET("public/get")
    suspend fun getProductList(): Response<List<ProductsItemDto>?>

    @Multipart
    @POST("public/add")
    suspend fun addProduct(
        @Part("product_name") productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("price") productPrice: RequestBody,
        @Part("tax") productTax: RequestBody,
        @Part productImage: MultipartBody.Part?,
    ): Response<AddProductRespDto>
}
