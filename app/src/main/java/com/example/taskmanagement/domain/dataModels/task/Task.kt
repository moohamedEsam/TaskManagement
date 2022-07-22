package com.example.taskmanagement.domain.dataModels.task

import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import com.example.taskmanagement.domain.utils.custom_serializers.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Task(
    val title: String,
    val owner: String = "",
    val description: String? = null,
    val assigned: List<ActiveUser> = emptyList(),
    val project: String,
    val status: TaskStatus = TaskStatus.Pending,
    val taskItems: List<TaskItem>,
    val comments: List<Comment> = emptyList(),
    val estimatedTime: Int? = null,
    val priority: Priority = Priority.Medium,
    @Serializable(with = DateSerializer::class)
    val completeDate: Date? = null,
    @Serializable(with = DateSerializer::class)
    val finishDate: Date? = null,
    val id: String = ""
)