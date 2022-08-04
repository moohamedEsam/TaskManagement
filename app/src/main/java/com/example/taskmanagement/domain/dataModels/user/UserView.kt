package com.example.taskmanagement.domain.dataModels.user

import com.example.taskmanagement.domain.dataModels.project.AbstractProject
import com.example.taskmanagement.domain.dataModels.task.AbstractTask
import com.example.taskmanagement.domain.dataModels.team.AbstractTeam
import kotlinx.serialization.Serializable

@Serializable
data class UserView(
    val publicId: String,
    val username: String,
    val phone: String?,
    val photoPath: String?,
    val projects: List<AbstractProject>,
    val tasks: List<AbstractTask>,
    val teams: List<AbstractTeam>
)

