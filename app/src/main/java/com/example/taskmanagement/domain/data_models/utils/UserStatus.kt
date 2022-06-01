package com.example.taskmanagement.domain.data_models.utils

sealed class UserStatus(val message: String? = null) {
    object Authorized : UserStatus()
    class Forbidden(message: String?) : UserStatus(message)
    object LoggedOut : UserStatus(null)
    object Loading : UserStatus(null) {

    }

}

