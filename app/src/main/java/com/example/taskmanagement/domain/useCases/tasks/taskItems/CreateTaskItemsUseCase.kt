package com.example.taskmanagement.domain.useCases.tasks.taskItems

import com.example.taskmanagement.domain.dataModels.task.TaskItem
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.base.BaseUseCaseBuilder

class CreateTaskItemsUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<List<TaskItem>, List<TaskItem>>() {
    override suspend fun build(params: List<TaskItem>): Resource<List<TaskItem>> {
        return repository.createTaskItems(params)
    }
}