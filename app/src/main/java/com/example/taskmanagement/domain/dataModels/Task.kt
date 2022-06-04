package com.example.taskmanagement.domain.dataModels

import com.example.taskmanagement.domain.utils.custom_serializers.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Task(
    val title: String,
    val owner: String = "",
    val description: String? = null,
    val assigned: MutableList<String>? = null,
    val project: String,
    val status: TaskStatus = TaskStatus.Pending,
    val taskItems: MutableList<TaskItem>,
    val comments: MutableList<Comment>? = null,
    val estimatedTime: Int? = null,
    val priority: Priority = Priority.Medium,
    @Serializable(with = DateSerializer::class)
    val completeDate: Date? = null,
    @Serializable(with = DateSerializer::class)
    val finishDate: Date? = null,
    @SerialName("publicId")
    val id: String
)