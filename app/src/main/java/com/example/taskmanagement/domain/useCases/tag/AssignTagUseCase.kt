package com.example.taskmanagement.domain.useCases.tag

import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import com.example.taskmanagement.domain.dataModels.utils.ParentRoute
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.base.BaseUseCaseBuilder

class AssignTagUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<AssignTagUseCase.Params, List<ActiveUser>>() {
    override suspend fun build(params: Params): Resource<List<ActiveUser>> {
        return repository.assignTag(params.parentId, params.parentRoute, params.members)
    }

    data class Params(
        val parentId: String,
        val parentRoute: ParentRoute,
        val members: List<ActiveUser>
    )
}