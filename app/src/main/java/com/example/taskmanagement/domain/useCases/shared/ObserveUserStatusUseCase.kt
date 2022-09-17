package com.example.taskmanagement.domain.useCases.shared

import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.UserStatus
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.base.BaseUseCaseBuilder
import kotlinx.coroutines.flow.SharedFlow

class ObserveUserStatusUseCase(private val repository: MainRepository):
    BaseUseCaseBuilder<Unit, SharedFlow<UserStatus>> {
    override suspend fun build(params: Unit): SharedFlow<UserStatus> {
        return repository.observeUser()
    }
}