package com.example.taskmanagement.domain.data_models.utils

import kotlinx.serialization.Serializable


@Serializable
data class Credentials(
    val email: String,
    val password: String
)
