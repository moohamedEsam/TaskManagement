package com.example.taskmanagement.domain.data_models.utils

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val username: String,
    val email: String,
    val password: String,
    val photoPath: String?,
    val phone: String?
)
