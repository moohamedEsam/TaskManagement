package com.example.taskmanagement.domain.dataModels.task

import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
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
    val id: String,
    val project: String,
    val title: String,
    val owner: User,
    val description: String?,
    val assigned: List<ActiveUserDto>,
    val status: TaskStatus,
    val estimatedTime: Int?,
    val priority: Priority,
    val taskItems: List<TaskItem>,
    val comments: List<Comment>,
    val isMileStone: Boolean = false,
    val mileStoneTitle: String = title,
    @Serializable(with = DateSerializer::class)
    val completeDate: Date?,
    @Serializable(with = DateSerializer::class)
    val finishDate: Date?
) {
    fun toTask() = Task(
        title = title,
        owner = owner.id,
        description = description,
        assigned = assigned.map { it.toActiveUser() },
        project = project,
        status = status,
        taskItems = taskItems,
        estimatedTime = estimatedTime,
        priority = priority,
        completeDate = completeDate,
        finishDate = finishDate,
        id = id
    )
}
