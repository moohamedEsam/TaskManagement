package com.example.taskmanagement.domain.dataModels

import com.example.taskmanagement.domain.dataModels.task.Permission
import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val permissions: List<Permission>,
    val title: String,
    val color: Long,
    val owner: String,
    val scope: TagScope,
    val id: String
)

enum class TagScope {
    Team,
    Project,
    Task
}

