package com.example.taskmanagement.domain.data_models.views

import com.example.taskmanagement.domain.data_models.User
import kotlinx.serialization.Serializable

@Serializable
data class ProjectView(
    val name: String,
    val owner: User,
    val description: String,
    val members: List<User>,
    val team: String,
    val publicId: String
)
