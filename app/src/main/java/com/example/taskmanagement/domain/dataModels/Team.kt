package com.example.taskmanagement.domain.dataModels

import kotlinx.serialization.Serializable

@Serializable
data class Team(
    val name: String,
    val description: String? = null,
    val owner: String = "",
    val members: List<ActiveUser>,
    val id: String = ""
)
