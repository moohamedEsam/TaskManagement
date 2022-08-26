package com.example.taskmanagement.domain.useCases.projects

import com.example.taskmanagement.domain.dataModels.project.Project
import com.example.taskmanagement.domain.dataModels.task.Task
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.BaseUseCaseBuilder

class GetCurrentUserProjectUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<Unit, Resource<List<Project>>> {
    override suspend fun build(params: Unit): Resource<List<Project>> {
        return repository.getCurrentUserProjects()
    }
}