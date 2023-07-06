package com.getswipe.android.ui.add_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getswipe.android.data.repo.GetSwipeRepoImpl
import com.getswipe.android.domain.model.ProductUploadModel
import com.getswipe.android.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddProductVM : ViewModel() {
    private val getSwipeRepo = GetSwipeRepoImpl()

    private var _addProductResource = MutableStateFlow<Resource<String>>(Resource.LOADING())
    val addProductResource get() = _addProductResource.asStateFlow()

    fun addProduct(productModel: ProductUploadModel) {
        viewModelScope.launch {
            _addProductResource.emit(Resource.LOADING())
            delay(200)
            _addProductResource.emit(getSwipeRepo.addProduct(productModel))
        }
    }
}
