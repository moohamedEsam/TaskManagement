package com.example.taskmanagement.domain.useCases.tasks.comments

import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.BaseUseCaseBuilder

class DeleteCommentUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<String, Boolean>() {
    override suspend fun build(params: String): Resource<Boolean> {
        return repository.deleteComment(params)
    }
}