package com.example.taskmanagement.domain.dataModels.utils

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmationResponse(
    val success: Boolean
)
