package com.example.taskmanagement.domain.dataModels.project

import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Project(
    val name: String,
    val owner: String = UUID.randomUUID().toString(),
    val description: String = "",
    val members: List<ActiveUser> = emptyList(),
    val team: String = UUID.randomUUID().toString(),
    val id: String = UUID.randomUUID().toString()
)
