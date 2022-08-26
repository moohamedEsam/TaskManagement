package com.example.taskmanagement.domain.useCases.user

import com.example.taskmanagement.domain.dataModels.task.Task
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.BaseUseCaseBuilder

class SearchMembersUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<String, Resource<List<User>>> {
    override suspend fun build(params: String): Resource<List<User>> {
        return repository.searchMembers(params)
    }
}