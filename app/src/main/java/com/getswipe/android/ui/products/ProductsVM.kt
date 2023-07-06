package com.getswipe.android.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getswipe.android.data.repo.GetSwipeRepoImpl
import com.getswipe.android.domain.model.ProductModel
import com.getswipe.android.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductsVM : ViewModel() {
    private val getSwipeRepo = GetSwipeRepoImpl()
    init {
        getProducts()
    }
    private var _productListResource =
        MutableStateFlow<Resource<List<ProductModel>>>(Resource.LOADING())
    val productListResource get() = _productListResource.asStateFlow()

    private fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _productListResource.emit(Resource.LOADING())
            delay(200)
            _productListResource.emit(getSwipeRepo.getProducts())
        }
    }
}
