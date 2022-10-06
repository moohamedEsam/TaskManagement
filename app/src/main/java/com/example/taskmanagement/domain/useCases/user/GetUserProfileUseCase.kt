package com.example.taskmanagement.domain.useCases.user

import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.base.BaseUseCaseBuilder

class GetUserProfileUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<String, Resource<User>> {
    override suspend fun build(params: String): Resource<User> {
        return repository.getUserProfile(params)
    }
}