package com.example.taskmanagement.domain.useCases.tasks.comments

import com.example.taskmanagement.domain.dataModels.task.Comment
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.base.BaseUseCaseBuilder

class CreateCommentUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<List<Comment>, Resource<List<Comment>>> {
    override suspend fun build(params: List<Comment>): Resource<List<Comment>> {
        return repository.createComments(params)
    }
}