package com.example.taskmanagement.domain.dataModels.team

import com.example.taskmanagement.domain.dataModels.user.User
import kotlinx.serialization.Serializable

@Serializable
data class TeamDto(
    val name: String,
    val description: String?,
    val owner: User,
    val members: List<User>,
    val id: String
)

