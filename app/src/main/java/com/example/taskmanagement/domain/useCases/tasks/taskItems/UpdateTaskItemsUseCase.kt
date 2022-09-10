package com.example.taskmanagement.domain.useCases.tasks.taskItems

import com.example.taskmanagement.domain.dataModels.task.Task
import com.example.taskmanagement.domain.dataModels.task.TaskItem
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.BaseUseCaseBuilder

class UpdateTaskItemsUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<UpdateTaskItemsUseCase.Params, List<TaskItem>>() {
    override suspend fun build(params: Params): Resource<List<TaskItem>> {
        return repository.updateTaskItems(params.taskItems, params.taskId)
    }

    data class Params(
        val taskId: String,
        val taskItems: List<String>
    )
}