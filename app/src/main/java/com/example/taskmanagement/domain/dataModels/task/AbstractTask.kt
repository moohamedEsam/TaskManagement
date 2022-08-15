package com.example.taskmanagement.domain.dataModels.task

import kotlinx.serialization.Serializable

@Serializable
data class AbstractTask(
    val title: String,
    val description: String,
    val id: String,
    val priority: Priority,
    val status: TaskStatus
)