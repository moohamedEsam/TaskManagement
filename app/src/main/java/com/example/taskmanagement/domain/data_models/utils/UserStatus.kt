package com.example.taskmanagement.domain.data_models.utils

import com.example.taskmanagement.domain.data_models.Token

sealed class UserStatus(val token: Token?, val message: String? = null) {
    class Authorized(token: Token) : UserStatus(token)
    class Forbidden(message: String?) : UserStatus(null, message)
    object LoggedOut : UserStatus(null, null)

}

