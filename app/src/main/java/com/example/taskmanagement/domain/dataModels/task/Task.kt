package com.example.taskmanagement.domain.dataModels.task

import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import com.example.taskmanagement.domain.utils.custom_serializers.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Task(
    val title: String,
    val owner: String = UUID.randomUUID().toString(),
    val description: String? = null,
    val assigned: List<String> = emptyList(),
    val project: String = UUID.randomUUID().toString(),
    val status: TaskStatus = TaskStatus.Pending,
    val taskItems: List<TaskItem> = emptyList(),
    val isMilestone: Boolean = false,
    val milestoneTitle: String = title,
    val estimatedTime: Int? = null,
    val priority: Priority = Priority.Medium,
    val isCompleted: Boolean = false,
    @Serializable(with = DateSerializer::class)
    val completeDate: Date? = null,
    @Serializable(with = DateSerializer::class)
    val finishDate: Date? = null,
    @Serializable(with = DateSerializer::class)
    val createdAt: Date = Date(),
    val id: String = UUID.randomUUID().toString()
)