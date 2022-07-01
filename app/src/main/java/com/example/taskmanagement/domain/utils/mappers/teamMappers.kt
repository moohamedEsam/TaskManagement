package com.example.taskmanagement.domain.utils.mappers

import com.example.taskmanagement.domain.dataModels.utils.CreateTeamBody
import com.example.taskmanagement.domain.dataModels.utils.TeamUser
import com.example.taskmanagement.domain.dataModels.views.TeamView

fun TeamView.toCreateTeamBody(): CreateTeamBody {
    return CreateTeamBody(
        name,
        description,
        owner.username,
        members.map { TeamUser(it.user.username, it.role) },
        id
    )
}