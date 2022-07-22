package com.example.taskmanagement.domain.useCases

import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.domain.dataModels.team.TeamDto
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.IMainRepository

class UpdateTeamUseCase(private val repository: IMainRepository) :
    BaseUseCaseBuilder<CreateTeamUseCase.Params, Resource<TeamDto>> {
    override suspend fun build(params: CreateTeamUseCase.Params): Resource<TeamDto> {
        val team = params.team
        return repository.updateTeam(team)
    }
}
