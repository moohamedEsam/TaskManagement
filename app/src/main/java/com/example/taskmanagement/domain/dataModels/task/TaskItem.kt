package com.example.taskmanagement.domain.dataModels.task

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TaskItem(
    val title: String,
    val isCompleted: Boolean = false,
    val completedBy: String? = null,
    val id: String = UUID.randomUUID().toString()
)
