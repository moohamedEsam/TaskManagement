package com.example.taskmanagement.domain.dataModels.team

import kotlinx.serialization.Serializable

@Serializable
data class AbstractTeam(
    val name: String,
    val description: String,
    val publicId: String
)