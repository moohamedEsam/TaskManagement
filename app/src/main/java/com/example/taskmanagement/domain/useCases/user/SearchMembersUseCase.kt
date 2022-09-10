package com.example.taskmanagement.domain.useCases.user

import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.BaseUseCaseBuilder

class SearchMembersUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<String, List<User>>() {
    private var lastQuery: String? = null
    private var lastResult = emptyList<User>()
    override suspend fun build(params: String): Resource<List<User>> {
        return if (lastQuery != null && params.contains(lastQuery!!))
            Resource.Success(lastResult.filter {
                it.email.contains(params) || it.username.contains(
                    params
                )
            })
        else {
            val result = repository.searchMembers(params)
            lastQuery = params
            lastResult = result.data ?: emptyList()
            result
        }
    }
}