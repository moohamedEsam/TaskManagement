package com.example.taskmanagement.domain.dataModels

import androidx.compose.ui.graphics.Color
import com.example.taskmanagement.domain.dataModels.task.Permission
import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val permissions: List<Permission> = emptyList(),
    val title: String = "",
    val color: List<Float> = listOf(0f, 0f, 0f),
    val owner: String = "",
    val id: String = ""
) {
    fun getColor() = Color(color[0], color[1], color[2])
    fun isUserAuthorized(vararg requiredPermission: Permission) =
        permissions.any { requiredPermission.contains(it) || it == Permission.FullControl }
}