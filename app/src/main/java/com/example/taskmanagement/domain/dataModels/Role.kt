package com.example.taskmanagement.domain.dataModels

import com.example.taskmanagement.domain.dataModels.Permission
import kotlinx.serialization.Serializable

@Serializable
data class Role(
    val title: String,
    val permissions: List<Permission>,
    val color: Long? = null,
    val owner: String = ""
)
