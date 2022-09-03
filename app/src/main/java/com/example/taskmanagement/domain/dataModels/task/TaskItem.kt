package com.example.taskmanagement.domain.dataModels.task

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TaskItem(
    val title: String,
    val completed: Boolean = false,
    val id:String = UUID.randomUUID().toString()
)
