package com.example.taskmanagement.domain.dataModels.task

import com.example.taskmanagement.domain.dataModels.task.Priority
import com.example.taskmanagement.domain.dataModels.task.TaskStatus
import kotlinx.serialization.Serializable

@Serializable
data class AbstractTask(
    val title: String,
    val description: String,
    val publicId: String,
    val priority: Priority,
    val status: TaskStatus
)