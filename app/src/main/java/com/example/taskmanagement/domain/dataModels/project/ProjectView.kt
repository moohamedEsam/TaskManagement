package com.example.taskmanagement.domain.dataModels.project

import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.task.AbstractTask
import com.example.taskmanagement.domain.dataModels.user.User
import kotlinx.serialization.Serializable

@Serializable
data class ProjectView(
    val name: String,
    val owner: User,
    val description: String,
    val members: List<ActiveUserDto>,
    val tasks: List<AbstractTask>,
    val tags: List<Tag>,
    val team: String,
    val id: String
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
