package com.example.taskmanagement.domain.useCases.tasks.taskItems

import com.example.taskmanagement.domain.dataModels.task.Task
import com.example.taskmanagement.domain.dataModels.task.TaskItem
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.BaseUseCaseBuilder

class DeleteTaskItemsUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<DeleteTaskItemsUseCase.Params, Resource<Boolean>> {
    override suspend fun build(params: Params): Resource<Boolean> {
        return repository.deleteTaskItems(params.taskItems, params.taskId)
    }

    data class Params(
        val taskId: String,
        val taskItems: List<String>
    )
}