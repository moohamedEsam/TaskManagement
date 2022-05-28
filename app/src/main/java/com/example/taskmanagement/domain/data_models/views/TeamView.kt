package com.example.taskmanagement.domain.data_models.views

import com.example.taskmanagement.domain.data_models.User
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class TeamView(
    val name: String,
    val description: String?,
    val owner: User,
    val members: List<User>,
    val publicId: String
)
