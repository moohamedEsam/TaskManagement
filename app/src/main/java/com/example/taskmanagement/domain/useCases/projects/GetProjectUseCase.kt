package com.example.taskmanagement.domain.useCases.projects

import com.example.taskmanagement.domain.dataModels.project.Project
import com.example.taskmanagement.domain.dataModels.project.ProjectView
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.BaseUseCaseBuilder

class GetProjectUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<String, Resource<ProjectView>> {
    override suspend fun build(params: String): Resource<ProjectView> {
        return repository.getProject(params)
    }
}
