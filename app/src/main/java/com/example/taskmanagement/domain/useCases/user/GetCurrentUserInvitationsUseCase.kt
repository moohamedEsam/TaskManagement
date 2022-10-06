package com.example.taskmanagement.domain.useCases.user

import com.example.taskmanagement.domain.dataModels.team.Invitation
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.base.BaseUseCaseBuilder

class GetCurrentUserInvitationsUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<Unit, Resource<List<Invitation>>> {
    override suspend fun build(params: Unit): Resource<List<Invitation>> {
        return repository.getInvitations()
    }
}