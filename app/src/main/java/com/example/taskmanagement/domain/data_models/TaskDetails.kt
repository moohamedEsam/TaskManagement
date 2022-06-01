package com.example.taskmanagement.domain.data_models

import com.example.taskmanagement.domain.utils.custom_serializers.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TaskDetails(
    val title: String,
    val description: String,
    val owner: User,
    val assigned: List<User>,
    val taskStatus: TaskStatus,
    val priority: Priority,
    @Serializable(with = DateSerializer::class)
    val finishDate: Date?,
    @Serializable(with = DateSerializer::class)
    val completeDate: Date?,
    val estimatedTime: Int?,
    val taskItems: List<TaskItem>,
    val comments: List<Comment>,
    val publicId: String
)

