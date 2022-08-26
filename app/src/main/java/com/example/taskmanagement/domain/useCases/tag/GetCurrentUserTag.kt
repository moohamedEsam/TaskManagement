package com.example.taskmanagement.domain.useCases.tag

import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.task.Task
import com.example.taskmanagement.domain.dataModels.utils.ParentRoute
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.BaseUseCaseBuilder

class GetCurrentUserTag(private val repository: MainRepository) :
    BaseUseCaseBuilder<GetCurrentUserTag.Params, Resource<Tag>> {
    override suspend fun build(params: Params): Resource<Tag> {
        return repository.getCurrentUserTag(params.parentRoute,params.id)
    }

    data class Params(
        val parentRoute: ParentRoute,
        val id:String
    )
}