package com.example.taskmanagement.domain.dataModels.project

import com.example.taskmanagement.domain.dataModels.user.User
import kotlinx.serialization.Serializable

@Serializable
data class ProjectSummery(
    val name: String,
    val id: String,
    val members: List<User>,
    val completedTasks: Int,
    val inProgressTasks: Int,
    val createdTasks: Int
)
