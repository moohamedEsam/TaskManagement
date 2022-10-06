package com.example.taskmanagement.domain.useCases.user

import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.base.BaseUseCaseBuilder

class DeclineInvitationUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<String, Resource<Boolean>> {
    override suspend fun build(params: String): Resource<Boolean> {
        return repository.declineInvitation(params)
    }
}