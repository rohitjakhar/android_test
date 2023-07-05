package com.getswipe.android.utils

sealed class Resource<T>(open val data: T? = null, open val message: String? = null) {
    data class SUCCESS<T>(override val data: T?) : Resource<T>(data, null)
    data class FAILURE<T>(override val message: String?) : Resource<T>(null, message)
    class LOADING<T>() : Resource<T>()
}
