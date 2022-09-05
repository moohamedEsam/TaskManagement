package com.example.taskmanagement.domain.useCases.user

import android.content.Context
import com.example.taskmanagement.domain.dataModels.utils.Credentials
import com.example.taskmanagement.domain.dataModels.utils.UserStatus
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.BaseUseCaseBuilder
import com.example.taskmanagement.presentation.koin.saveToken

class LoginUserUseCase(
    private val repository: MainRepository,
    private val context: Context
) :
    BaseUseCaseBuilder<LoginUserUseCase.Params, UserStatus> {
    override suspend fun build(params: Params): UserStatus {
        val result = repository.loginUser(params.credentials)
        if (result is UserStatus.Authorized)
            saveToken(context, result.token ?: return result)
        return result
    }

    data class Params(
        val credentials: Credentials
    )
}