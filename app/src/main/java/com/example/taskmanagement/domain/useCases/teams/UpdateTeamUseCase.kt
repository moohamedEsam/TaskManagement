package com.example.taskmanagement.domain.useCases.teams

import com.example.taskmanagement.domain.dataModels.team.TeamDto
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.base.BaseUseCaseBuilder

class UpdateTeamUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<CreateTeamUseCase.Params, Resource<TeamDto>> {
    override suspend fun build(params: CreateTeamUseCase.Params): Resource<TeamDto> {
        val team = params.team
        return repository.updateTeam(team)
    }
}
