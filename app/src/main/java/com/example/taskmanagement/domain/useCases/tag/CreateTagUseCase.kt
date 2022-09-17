package com.example.taskmanagement.domain.useCases.tag

import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.utils.ParentRoute
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.base.BaseUseCaseBuilder

class CreateTagUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<CreateTagUseCase.Params, Resource<Tag>> {
    override suspend fun build(params: Params): Resource<Tag> {
        return repository.createTag(params.tag, params.parent)
    }

    data class Params(
        val tag: Tag,
        val parent: ParentRoute
    )
}