package com.example.taskmanagement.domain.dataModels.team

import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.project.ProjectSummery
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TeamView(
    val name: String,
    val description: String? = null,
    val owner: User = User(username = "owner"),
    val members: List<ActiveUserDto> = emptyList(),
    val pendingMembers: List<User> = emptyList(),
    val projects: List<ProjectSummery> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val id: String = UUID.randomUUID().toString()
) {
    fun toTeam(): Team {
        return Team(
            name = name,
            description = description,
            owner = owner.id,
            members = members.map { it.toActiveUser() },
            id = id
        )
    }
}
