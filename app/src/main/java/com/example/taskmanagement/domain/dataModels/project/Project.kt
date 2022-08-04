package com.example.taskmanagement.domain.dataModels.project

import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val name: String,
    val owner: String = "",
    val description: String = "",
    val members: List<ActiveUser>,
    val team: String,
    val id: String = ""
)
