package com.example.taskmanagement.domain.dataModels

import kotlinx.serialization.Serializable

@Serializable
data class Role(
    val permissions: List<Permission>
)
