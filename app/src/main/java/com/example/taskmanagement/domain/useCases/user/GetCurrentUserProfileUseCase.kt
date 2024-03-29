package com.example.taskmanagement.domain.useCases.user

import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.base.BaseUseCaseBuilder

class GetCurrentUserProfileUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<Unit, Resource<User>> {
    override suspend fun build(params: Unit): Resource<User> {
        return repository.getCurrentUserProfile()
    }
}