package com.example.taskmanagement.domain.dataModels

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val name: String,
    val owner: String = "",
    val description: String = "",
    val members: List<ActiveUser>,
    val team: String,
     val id: String = ""
)
