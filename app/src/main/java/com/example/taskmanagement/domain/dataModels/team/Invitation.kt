package com.example.taskmanagement.domain.dataModels.team

import kotlinx.serialization.Serializable

@Serializable
data class Invitation(
    val teamName: String,
    val source: String,
    val to: String,
   val id: String
)