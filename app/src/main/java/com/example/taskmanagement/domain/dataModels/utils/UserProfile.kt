package com.example.taskmanagement.domain.dataModels.utils

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val username: String,
    val email: String,
    val password: String,
    val photoPath: String?,
    val phone: String?
)
