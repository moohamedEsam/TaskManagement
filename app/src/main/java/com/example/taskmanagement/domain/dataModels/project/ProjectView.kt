package com.example.taskmanagement.domain.dataModels.project

import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.task.Task
import com.example.taskmanagement.domain.dataModels.user.User
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class ProjectView(
    val name: String,
    val owner: User = User("owner"),
    val description: String = "",
    val members: List<ActiveUserDto> = emptyList(),
    val tasks: List<Task> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val team: String = UUID.randomUUID().toString(),
    val id: String = UUID.randomUUID().toString()
) {
    fun toProject() = Project(
        name = name,
        owner = owner.id,
        description = description,
        members = members.map { it.toActiveUser() },
        team = team,
        id = id
    )
}
