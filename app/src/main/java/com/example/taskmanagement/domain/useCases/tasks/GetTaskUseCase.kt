package com.example.taskmanagement.domain.useCases.tasks

import com.example.taskmanagement.domain.dataModels.task.Task
import com.example.taskmanagement.domain.dataModels.task.TaskView
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.BaseUseCaseBuilder

class GetTaskUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<String, Resource<TaskView>> {
    override suspend fun build(params: String): Resource<TaskView> {
        return repository.getTask(params)
    }
}
