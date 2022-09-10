package com.example.taskmanagement.domain.useCases.projects

import com.example.taskmanagement.domain.dataModels.project.Project
import com.example.taskmanagement.domain.dataModels.project.ProjectView
import com.example.taskmanagement.domain.dataModels.task.Task
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.BaseUseCaseBuilder
import com.example.taskmanagement.presentation.koin.repository

class CreateProjectUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<Project, Project>() {
    override suspend fun build(params: Project): Resource<Project> {
        return repository.createProject(params)
    }
}