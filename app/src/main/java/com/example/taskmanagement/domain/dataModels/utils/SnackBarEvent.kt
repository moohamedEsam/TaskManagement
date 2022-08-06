package com.example.taskmanagement.domain.dataModels.utils

data class SnackBarEvent(
    val message: String,
    val actionTitle: String? = "Retry",
    val action: suspend () -> Unit
)