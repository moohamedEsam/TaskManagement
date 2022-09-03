package com.example.taskmanagement.domain.dataModels.activeUser

import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.user.User
import kotlinx.serialization.Serializable

@Serializable
data class ActiveUser(
    val id: String,
    val tag: String? = null
)

