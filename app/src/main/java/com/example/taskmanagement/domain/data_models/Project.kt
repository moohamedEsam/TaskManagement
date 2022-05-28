package com.example.taskmanagement.domain.data_models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val name: String,
    val owner: String = "",
    val description: String,
    val members: List<String>,
    val team: String,
    @SerialName("publicId")
    val id: String
)
