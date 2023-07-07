package com.getswipe.android.ui.add_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getswipe.android.domain.model.ProductUploadModel
import com.getswipe.android.domain.repo.GetSwipeRepo
import com.getswipe.android.utils.NetworkHelper
import com.getswipe.android.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddProductVM(
    private val getSwipeRepo: GetSwipeRepo,
    private val networkHelper: NetworkHelper,
) : ViewModel() {

    private var _addProductResource = MutableStateFlow<Resource<String>>(Resource.LOADING())
    val addProductResource get() = _addProductResource.asStateFlow()

    fun addProduct(productModel: ProductUploadModel) {
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                _addProductResource.emit(Resource.LOADING())
                delay(800)
                _addProductResource.emit(getSwipeRepo.addProduct(productModel))
            } else {
                _addProductResource.emit(Resource.FAILURE("Network not found!"))
            }
        }
    }
}
