package com.example.taskmanagement.domain.dataModels.project

import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.task.AbstractTask
import kotlinx.serialization.Serializable

@Serializable
data class ProjectView(
    val name: String,
    val owner: User,
    val description: String,
    val members: List<ActiveUser>,
    val tasks: List<AbstractTask>,
    val team: String,
    val id: String
)
