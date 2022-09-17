package com.example.taskmanagement.domain.useCases.tasks

import com.example.taskmanagement.domain.dataModels.task.Task
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.base.BaseUseCaseBuilder

class GetCurrentUserTasksUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<Unit, Resource<List<Task>>> {
    override suspend fun build(params: Unit): Resource<List<Task>> {
        return repository.getCurrentUserTasks()
    }
}