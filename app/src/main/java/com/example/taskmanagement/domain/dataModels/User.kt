package com.example.taskmanagement.domain.dataModels


import kotlinx.serialization.Serializable


@Serializable
data class User(
    val username: String,
    val email: String = "",
    val photoPath: String?,
    val phone: String?,
    val id: String
)