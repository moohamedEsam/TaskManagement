package com.example.taskmanagement.domain.dataModels

import androidx.compose.ui.graphics.Color
import com.example.taskmanagement.domain.dataModels.task.Permission
import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val permissions: List<Permission>,
    val title: String,
    val color: List<Float>,
    val scope: TagScope,
    val owner: String,
    val id: String
) {
    fun getColor() = Color(color[0], color[1], color[2])
    fun isUserAuthorized(requiredPermission: Permission) =
        permissions.any { it == requiredPermission || it == Permission.FullControl }
}

enum class TagScope {
    Team,
    Project,
    Task
}
