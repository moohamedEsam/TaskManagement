package com.example.taskmanagement.domain.data_models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String,
    val phone: String?,
    val photoPath: String?
)
