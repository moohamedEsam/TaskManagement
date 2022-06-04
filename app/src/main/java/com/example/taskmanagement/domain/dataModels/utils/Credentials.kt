package com.example.taskmanagement.domain.dataModels.utils

import kotlinx.serialization.Serializable


@Serializable
data class Credentials(
    val email: String,
    val password: String
)
