package com.example.taskmanagement.domain.useCases.teams.invitation

import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.base.BaseUseCaseBuilder

class SendInvitationUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<SendInvitationUseCase.Param, Resource<Boolean>> {
    override suspend fun build(params: Param): Resource<Boolean> {
        return repository.sendInvitations(params.teamId, params.invitation)
    }

    data class Param(
        val teamId: String,
        val invitation: List<String>
    )
}