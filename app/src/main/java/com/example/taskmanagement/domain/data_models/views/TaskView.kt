package com.example.taskmanagement.domain.data_models.views

import com.example.taskmanagement.domain.data_models.Priority
import com.example.taskmanagement.domain.data_models.Comment
import com.example.taskmanagement.domain.data_models.TaskItem
import com.example.taskmanagement.domain.data_models.TaskStatus
import com.example.taskmanagement.domain.data_models.User
import com.example.taskmanagement.domain.utils.custom_serializers.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TaskView(
    val publicId: String,
    val name: String,
    val owner: User,
    val description: String?,
    val assigned: List<User>?,
    val taskStatus: TaskStatus,
    val estimatedTime: Int?,
    val priority: Priority,
    val taskItems: List<TaskItem>?,
    val comments: List<Comment>?,
    @Serializable(with = DateSerializer::class)
    val completedDate: Date?,
    @Serializable(with = DateSerializer::class)
    val finishDate: Date?
)
