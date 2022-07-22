package com.example.taskmanagement.domain.dataModels.project

import kotlinx.serialization.Serializable

@Serializable
data class AbstractProject(
    val name: String,
    val description: String,
    val publicId: String
)