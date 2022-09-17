package com.example.taskmanagement.domain.dataModels.user

import com.example.taskmanagement.domain.dataModels.project.Project
import com.example.taskmanagement.domain.dataModels.task.Task
import com.example.taskmanagement.domain.dataModels.team.Team
import kotlinx.serialization.Serializable

@Serializable
data class Dashboard(
    val id:String,
    val tasks:List<Task>,
    val projects:List<Project>,
    val teams:List<Team>
)
