package com.getswipe.android.di

import android.content.Context
import com.getswipe.android.data.webservice.GetSwipeService
import com.getswipe.android.utils.Constant
import com.getswipe.android.utils.NetworkHelper
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
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

fun provideNetworkHelper(context: Context) = NetworkHelper(context)

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
    single { provideNetworkHelper(androidContext()) }
}
