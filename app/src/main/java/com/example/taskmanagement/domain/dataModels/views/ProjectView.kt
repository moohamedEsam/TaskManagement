package com.example.taskmanagement.domain.dataModels.views

import com.example.taskmanagement.domain.dataModels.ActiveUser
import com.example.taskmanagement.domain.dataModels.User
import com.example.taskmanagement.domain.dataModels.abstarct.AbstractTask
import kotlinx.serialization.Serializable

@Serializable
data class ProjectView(
    val name: String,
    val owner: User,
    val description: String,
    val members: List<ActiveUser>,
    val tasks: List<AbstractTask>,
    val team: String,
    val publicId: String
)
