package com.example.taskmanagement.domain.data_models.utils

sealed class Resource<T>(val data: T?, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)

    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)

    class Initialized<T>() : Resource<T>(null, null)

    suspend fun onSuccess(code: suspend (T) -> Unit) {
        if (this is Success && data != null)
            code(data)
    }
}
