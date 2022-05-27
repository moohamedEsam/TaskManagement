package com.example.taskmanagement.domain.data_models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Team(
    @SerialName("publicId")
    val id: String,
    val name: String,
    val description: String?,
    val members: List<String>,
)
