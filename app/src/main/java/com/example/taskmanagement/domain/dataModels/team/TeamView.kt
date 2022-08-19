package com.example.taskmanagement.domain.dataModels.team

import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.project.ProjectSummery
import kotlinx.serialization.Serializable

@Serializable
data class TeamView(
    val name: String,
    val description: String?,
    val owner: User,
    val members: List<ActiveUserDto>,
    val pendingMembers: List<User>,
    val projects: List<ProjectSummery>,
    val tags: List<com.example.taskmanagement.domain.dataModels.Tag>,
    val id: String
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
