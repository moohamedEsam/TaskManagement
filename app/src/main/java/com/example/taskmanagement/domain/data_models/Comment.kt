package com.example.taskmanagement.domain.data_models

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: String,
    val owner: String,
    val description: String,
    val mentions: List<String>?,
)
