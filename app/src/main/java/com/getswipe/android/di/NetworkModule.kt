package com.getswipe.android.di

import com.getswipe.android.data.webservice.GetSwipeService
import com.getswipe.android.utils.Constant
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun provideOkHttpClient(): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)
    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
    return client
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .build()
}

fun provideGetSwipeService(retrofit: Retrofit): GetSwipeService {
    return retrofit.create(GetSwipeService::class.java)
}
val networkModule = module {
    single {
        provideOkHttpClient()
    }
    single {
        provideRetrofit(get())
    }
    single {
        provideGetSwipeService(get())
    }
}
