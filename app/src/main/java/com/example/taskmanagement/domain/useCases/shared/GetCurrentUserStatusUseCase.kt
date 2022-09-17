package com.example.taskmanagement.domain.useCases.shared

import android.content.Context
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.UserStatus
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.base.BaseUseCaseBuilder
import com.example.taskmanagement.presentation.koin.loadToken

class GetCurrentUserStatusUseCase(
    private val context: Context,
    private val repository: MainRepository
) : BaseUseCaseBuilder<Unit, UserStatus> {
    override suspend fun build(params: Unit): UserStatus {
        val token = loadToken(context)
        if (token.token.isEmpty()) {
            repository.setUserStatus(UserStatus.LoggedOut)
            return UserStatus.LoggedOut
        }
        repository.setUserStatus(UserStatus.Authorized(token))
        return UserStatus.Authorized(token)
    }
}