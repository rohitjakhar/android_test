package com.getswipe.android.data.webservice

import com.getswipe.android.data.dto.AddProductRespDto
import com.getswipe.android.data.dto.ProductsItemDto
import com.getswipe.android.utils.Constant
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST

interface GetSwipeService {
    @GET("public/get")
    suspend fun getProductList(): Response<List<ProductsItemDto>?>

    @POST("public/add")
    suspend fun addProduct(): Response<AddProductRespDto>

    companion object {
        fun getGetSwipeService(): GetSwipeService {
            val loggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .build()
                .create(GetSwipeService::class.java)
        }
    }
}
