package com.example.taskmanagement.domain.dataModels.abstarct

import com.example.taskmanagement.domain.dataModels.Priority
import com.example.taskmanagement.domain.dataModels.TaskStatus
import kotlinx.serialization.Serializable

@Serializable
data class AbstractTask(
    val title: String,
    val description: String,
    val publicId: String,
    val priority: Priority,
    val status: TaskStatus
)