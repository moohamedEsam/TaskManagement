package com.example.taskmanagement.domain.dataModels.views

import com.example.taskmanagement.domain.dataModels.ActiveUser
import com.example.taskmanagement.domain.dataModels.User
import com.example.taskmanagement.domain.dataModels.abstarct.AbstractProject
import kotlinx.serialization.Serializable

@Serializable
data class TeamView(
    val name: String,
    val description: String?,
    val owner: User,
    val members: List<ActiveUser>,
    val projects: List<AbstractProject>,
    val id: String
)
