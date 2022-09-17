package com.example.taskmanagement.domain.useCases.user

import com.example.taskmanagement.domain.dataModels.user.Dashboard
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.base.BaseUseCaseBuilder

class GetCurrentUserDashboardUseCase(private val repository: MainRepository) :
    BaseUseCaseBuilder<Unit, Dashboard>() {
    override suspend fun build(params: Unit): Resource<Dashboard> {
        return repository.getCurrentUserDashboard()
    }
}