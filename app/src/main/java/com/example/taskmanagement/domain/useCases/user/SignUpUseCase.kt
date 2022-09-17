package com.example.taskmanagement.domain.useCases.user

import android.content.Context
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.SignUpUserBody
import com.example.taskmanagement.domain.dataModels.utils.Token
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.base.BaseUseCaseBuilder
import com.example.taskmanagement.presentation.koin.saveToken

class SignUpUseCase(
    private val repository: MainRepository,
    private val context: Context
) : BaseUseCaseBuilder<SignUpUseCase.Params, Token>() {
    override suspend fun build(params: Params): Resource<Token> {
        val result = repository.registerUser(params.signUpUserBody)
        result.onSuccess {
            saveToken(context, it)
        }
        return result

    }

    data class Params(
        val signUpUserBody: SignUpUserBody
    )
}