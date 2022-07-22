package com.example.taskmanagement.domain.dataModels.utils


import kotlinx.serialization.Serializable


@Serializable
data class SignUpUserBody(
    val username: String,
    val email: String,
    val photoPath: String?,
    val phone: String?,
    val password: String
)
