package com.example.taskmanagement.domain.dataModels.views

import com.example.taskmanagement.domain.dataModels.Priority
import com.example.taskmanagement.domain.dataModels.Comment
import com.example.taskmanagement.domain.dataModels.TaskItem
import com.example.taskmanagement.domain.dataModels.TaskStatus
import com.example.taskmanagement.domain.dataModels.User
import com.example.taskmanagement.domain.utils.custom_serializers.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TaskView(
    val publicId: String,
    val title: String,
    val owner: User,
    val description: String?,
    val assigned: List<User>?,
    val status: TaskStatus,
    val estimatedTime: Int?,
    val priority: Priority,
    val taskItems: List<TaskItem>?,
    val comments: List<Comment>?,
    @Serializable(with = DateSerializer::class)
    val completeDate: Date?,
    @Serializable(with = DateSerializer::class)
    val finishDate: Date?
)
