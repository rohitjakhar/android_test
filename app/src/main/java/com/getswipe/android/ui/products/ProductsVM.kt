package com.getswipe.android.ui.products

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.getswipe.android.domain.model.ProductModel
import com.getswipe.android.domain.repo.GetSwipeRepo
import com.getswipe.android.utils.NetworkHelper
import com.getswipe.android.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductsVM(
    private val getSwipeRepo: GetSwipeRepo,
    private val networkHelper: NetworkHelper,
) : ViewModel() {
    init {
        getProducts()
    }
    private var _productListResource =
        MutableStateFlow<Resource<List<ProductModel>>>(Resource.LOADING())
    val productListResource get() = _productListResource.asStateFlow()

    val searchText = MutableLiveData("")

    val searchData = searchText.switchMap { searchValue ->
        liveData {
            emit(productListResource.value.data?.filter { it.search(searchValue) })
        }
    }
    private fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            if (networkHelper.isNetworkConnected()) {
                _productListResource.emit(Resource.LOADING())
                delay(200)
                _productListResource.emit(getSwipeRepo.getProducts())
            } else {
                _productListResource.emit(Resource.FAILURE(message = "Network not found!"))
            }
        }
    }
}
