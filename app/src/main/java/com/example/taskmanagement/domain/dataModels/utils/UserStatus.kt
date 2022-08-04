package com.example.taskmanagement.domain.dataModels.utils

sealed class UserStatus(val token: Token? = null, val message: String? = null) {
    class Authorized(token: Token) : UserStatus(token)
    class Forbidden(message: String?) : UserStatus(message = message)
    object LoggedOut : UserStatus(null)
    object Loading : UserStatus(null) {

    }

}

