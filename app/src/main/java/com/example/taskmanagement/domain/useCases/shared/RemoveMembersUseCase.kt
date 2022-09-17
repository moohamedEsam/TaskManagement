package com.example.taskmanagement.domain.useCases.shared

import com.example.taskmanagement.domain.dataModels.utils.ParentRoute
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.base.BaseUseCaseBuilder

class RemoveMembersUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<RemoveMembersUseCase.Params, Resource<Boolean>> {
    override suspend fun build(params: Params): Resource<Boolean> {
        return repository.removeMembers(params.parentRoute, params.parentId, params.members)
    }

    data class Params(
        val parentId: String,
        val parentRoute: ParentRoute,
        val members: List<String>
    )
}