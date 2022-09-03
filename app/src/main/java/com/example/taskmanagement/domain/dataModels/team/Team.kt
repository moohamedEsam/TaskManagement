package com.example.taskmanagement.domain.dataModels.team

import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Team(
    val name: String,
    val description: String? = null,
    val owner: String = UUID.randomUUID().toString(),
    var members: List<ActiveUser> = emptyList(),
    val id: String = UUID.randomUUID().toString()
)
