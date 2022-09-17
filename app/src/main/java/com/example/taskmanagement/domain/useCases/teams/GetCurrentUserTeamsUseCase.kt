package com.example.taskmanagement.domain.useCases.teams

import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.base.BaseUseCaseBuilder

class GetCurrentUserTeamsUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<Unit, List<Team>>() {
    override suspend fun build(params: Unit): Resource<List<Team>> {
        return repository.getCurrentUserTeams()
    }
}