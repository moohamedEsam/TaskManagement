package com.example.taskmanagement.domain.dataModels.task

import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import com.example.taskmanagement.domain.dataModels.task.Comment
import com.example.taskmanagement.domain.dataModels.task.Priority
import com.example.taskmanagement.domain.dataModels.task.TaskItem
import com.example.taskmanagement.domain.dataModels.task.TaskStatus
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.utils.custom_serializers.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TaskView(
    val publicId: String,
    val title: String,
    val owner: User,
    val description: String?,
    val assigned: List<ActiveUser>?,
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
