package com.example.taskmanagement.domain.dataModels

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmationResponse(
    val success: Boolean
)
