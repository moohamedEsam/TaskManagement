package com.example.taskmanagement.domain.dataModels.team

import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import kotlinx.serialization.Serializable

@Serializable
data class Team(
    val name: String,
    val description: String? = null,
    val owner: String = "",
    var members: List<ActiveUser>,
    val id: String = ""
)
