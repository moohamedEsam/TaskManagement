package com.example.taskmanagement.domain.dataModels.team

import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.project.AbstractProject
import kotlinx.serialization.Serializable

@Serializable
data class TeamView(
    val name: String,
    val description: String?,
    val owner: User,
    val members: List<ActiveUserDto>,
    val projects: List<AbstractProject>,
    val id: String
) {
    fun toTeam(): Team {
        return Team(
            name,
            description,
            owner.username,
            members.map { it.toActiveUser() },
            id
        )
    }
}
