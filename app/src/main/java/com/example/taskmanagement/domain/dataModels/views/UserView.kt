package com.example.taskmanagement.domain.dataModels.views

import com.example.taskmanagement.domain.dataModels.abstarct.*
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

