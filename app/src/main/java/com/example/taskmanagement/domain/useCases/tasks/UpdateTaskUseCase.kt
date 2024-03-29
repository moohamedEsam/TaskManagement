package com.example.taskmanagement.domain.useCases.tasks

import com.example.taskmanagement.domain.dataModels.task.Task
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.base.BaseUseCaseBuilder

class UpdateTaskUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<Task, Resource<Task>> {
    override suspend fun build(params: Task): Resource<Task> {
        return repository.updateTask(params)
    }
}