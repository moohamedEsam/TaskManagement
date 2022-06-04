package com.example.taskmanagement.domain.dataModels

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String,
    val phone: String?,
    val photoPath: String?,
    val publicId: String
)
