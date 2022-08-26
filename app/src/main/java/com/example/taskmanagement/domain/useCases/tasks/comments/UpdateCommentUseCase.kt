package com.example.taskmanagement.domain.useCases.tasks.comments

import com.example.taskmanagement.domain.dataModels.task.Comment
import com.example.taskmanagement.domain.dataModels.task.Task
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.BaseUseCaseBuilder

class UpdateCommentUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<Comment, Resource<Comment>> {
    override suspend fun build(params: Comment): Resource<Comment> {
        return repository.updateComment(params)
    }
}