package com.example.taskmanagement.domain.useCases

import android.content.Context
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.SignUpUserBody
import com.example.taskmanagement.domain.dataModels.utils.UserStatus
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.presentation.koin.saveToken

class SignUpUseCase(
    private val repository: MainRepository,
) : BaseUseCaseBuilder<SignUpUseCase.Params, UserStatus> {
    override suspend fun build(params: Params): UserStatus {
        val result = repository.registerUser(params.signUpUserBody)
        return if (result is Resource.Success && result.data != null) {
            saveToken(params.context, result.data)
            UserStatus.Authorized(result.data)
        } else
        UserStatus.Forbidden(result.message)

    }

    data class Params(
        val signUpUserBody: SignUpUserBody,
        val context: Context
    )
}