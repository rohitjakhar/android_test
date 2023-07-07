package com.getswipe.android.di

import com.getswipe.android.ui.add_product.AddProductVM
import com.getswipe.android.ui.products.ProductsVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val productsVMModule = module {
    viewModel {
        ProductsVM(get(), get())
    }
}

val addProductVMModule = module {
    viewModel {
        AddProductVM(get(), get())
    }
}
