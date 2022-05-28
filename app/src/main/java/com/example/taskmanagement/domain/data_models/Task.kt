package com.example.taskmanagement.domain.data_models

import com.example.taskmanagement.domain.utils.custom_serializers.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Task(
    val name: String,
    val owner: String = "",
    val description: String? = null,
    val assigned: MutableList<String>? = null,
    val project: String,
    val taskStatus: TaskStatus = TaskStatus.Pending,
    val taskItems: MutableList<TaskItem>,
    val comments: MutableList<Comment>? = null,
    val estimatedTime: Int? = null,
    val priority: Priority = Priority.MEDIUM,
    @Serializable(with = DateSerializer::class)
    val completedDate: Date? = null,
    @Serializable(with = DateSerializer::class)
    val finishDate: Date? = null,
    @SerialName("publicId")
    val id: String
)