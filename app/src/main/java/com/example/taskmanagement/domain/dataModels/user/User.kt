package com.example.taskmanagement.domain.dataModels.user


import kotlinx.serialization.Serializable
import java.util.*


@Serializable
data class User(
    val username: String = "",
    val email: String = "",
    val photoPath: String? = null,
    val phone: String? = null,
    val id: String = UUID.randomUUID().toString()
)
