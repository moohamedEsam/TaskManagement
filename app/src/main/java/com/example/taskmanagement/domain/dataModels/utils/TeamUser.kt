package com.example.taskmanagement.domain.dataModels.utils

import com.example.taskmanagement.domain.dataModels.Role
import kotlinx.serialization.Serializable

@Serializable
data class TeamUser(
    val username:String,
    val role: Role
)
