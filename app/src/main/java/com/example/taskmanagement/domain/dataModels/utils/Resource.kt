package com.example.taskmanagement.domain.dataModels.utils

sealed class Resource<T>(val data: T?, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)

    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)

    class Initialized<T>() : Resource<T>(null, null)
    class Loading<T> : Resource<T>(null, null)

    inline fun onSuccess(code: (T) -> Unit) {
        if (this is Success && data != null)
            code(data)
    }

    inline fun onError(code: (String?) -> Unit) {
        if (this is Error)
            code(message)
    }


    fun copy(data: T? = this.data): Resource<T> {
        return when (this) {
            is Success -> if (data != null) Success(data) else Initialized()
            is Error -> Error(message, data)
            is Initialized -> Initialized()
            else -> Loading()
        }
    }
}
