package com.example.taskmanagement.domain.data_models

import com.example.taskmanagement.domain.utils.custom_serializers.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Task(
    @SerialName("publicId")
    val id: String,
    val title: String,
    val description: String,
    val owner: String,
    val assigned: List<String>,
    val status: TaskStatus,
    @Serializable(with = DateSerializer::class)
    val finishDate: Date?,
    @Serializable(with = DateSerializer::class)
    val completeDate: Date?,
    val estimatedTime: Int?,
    val taskItems: List<TaskItem>,
    val comments: List<Comment>,
    val priority: TaskPriority

)
