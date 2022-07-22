package com.example.taskmanagement.domain.useCases

import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.domain.dataModels.team.TeamDto
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.IMainRepository

class CreateTeamUseCase(private val repository: IMainRepository) :
    BaseUseCaseBuilder<CreateTeamUseCase.Params, Resource<TeamDto>> {
    override suspend fun build(params: Params): Resource<TeamDto> {
        val team = params.team
        return repository.createTeam(team)
    }

    data class Params(
        val team: Team
    )
}
