package com.example.taskmanagement.domain.dataModels.task

import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.HistoryItem
import com.example.taskmanagement.domain.utils.custom_serializers.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TaskView(
    val title: String,
    val id: String = UUID.randomUUID().toString(),
    val project: String = UUID.randomUUID().toString(),
    val owner: User = User(""),
    val description: String? = null,
    val assigned: MutableList<User> = mutableListOf(),
    val status: TaskStatus = TaskStatus.Pending,
    val estimatedTime: Int? = null,
    val priority: Priority = Priority.Medium,
    val taskItems: List<TaskItem> = emptyList(),
    val comments: MutableList<CommentView> = mutableListOf(),
    val isMilestone: Boolean = false,
    val milestoneTitle: String = title,
    @Serializable(with = DateSerializer::class)
    val completeDate: Date? = null,
    @Serializable(with = DateSerializer::class)
    val createdAt: Date = Date(),
    @Serializable(with = DateSerializer::class)
    val finishDate: Date? = null,
    val history: List<HistoryItem> = emptyList(),
) {
    fun toTask() = Task(
        title = title,
        owner = owner.id,
        description = description,
        assigned = assigned.map { it.id },
        project = project,
        status = status,
        taskItems = taskItems,
        estimatedTime = estimatedTime,
        priority = priority,
        completeDate = completeDate,
        finishDate = finishDate,
        id = id,
        isMilestone = isMilestone,
        milestoneTitle = milestoneTitle,
        createdAt = createdAt
    )
}
