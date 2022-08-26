package com.example.taskmanagement.domain.useCases.teams

import com.example.taskmanagement.domain.dataModels.task.TaskView
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.BaseUseCaseBuilder

class GetTeamUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<String, Resource<TeamView>> {
    override suspend fun build(params: String): Resource<TeamView> {
        return repository.getTeam(params)
    }
}
