package com.getswipe.android

import android.app.Application
import com.getswipe.android.di.addProductVMModule
import com.getswipe.android.di.networkModule
import com.getswipe.android.di.productsVMModule
import com.getswipe.android.di.repoModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            androidLogger(Level.DEBUG)
            modules(listOf(networkModule, repoModule, productsVMModule, addProductVMModule))
        }
    }
}
