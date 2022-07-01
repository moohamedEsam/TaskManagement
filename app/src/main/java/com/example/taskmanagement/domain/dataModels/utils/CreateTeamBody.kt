package com.example.taskmanagement.domain.dataModels.utils

import kotlinx.serialization.Serializable

@Serializable
data class CreateTeamBody(
    val name: String,
    val description: String?,
    val owner: String,
    val members: List<TeamUser>,
    val id: String = ""
)
