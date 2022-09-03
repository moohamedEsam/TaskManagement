package com.example.taskmanagement.domain.dataModels.team

import com.example.taskmanagement.domain.dataModels.user.User
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TeamDto(
    val name: String ,
    val description: String? = null,
    val owner: User,
    val members: List<User> = emptyList(),
    val id: String = UUID.randomUUID().toString()
)

