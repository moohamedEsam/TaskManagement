package com.example.taskmanagement.domain.dataModels.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val token: String,
    @SerialName("expiresIn")
    val expiresAt:Long
)
