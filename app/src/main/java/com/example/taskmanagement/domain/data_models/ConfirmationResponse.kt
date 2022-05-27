package com.example.taskmanagement.domain.data_models

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmationResponse(
    val success: Boolean
)
