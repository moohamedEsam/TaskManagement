package com.example.taskmanagement.domain.dataModels

import kotlinx.serialization.Serializable

@Serializable
data class ActiveUser(
    val user: User,
    val role: Role
)

