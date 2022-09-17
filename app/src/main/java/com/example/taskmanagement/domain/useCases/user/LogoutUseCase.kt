package com.example.taskmanagement.domain.useCases.user

import android.content.Context
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.Token
import com.example.taskmanagement.domain.dataModels.utils.UserStatus
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.base.BaseUseCaseBuilder
import com.example.taskmanagement.presentation.koin.saveToken

class LogoutUseCase(private val repository: MainRepository, private val context: Context) :
    BaseUseCaseBuilder<Unit, Resource<Boolean>> {
    override suspend fun build(params: Unit): Resource<Boolean> {
        saveToken(context, Token("", 0))
        return Resource.Success(repository.logoutUser() is UserStatus.LoggedOut)
    }
}