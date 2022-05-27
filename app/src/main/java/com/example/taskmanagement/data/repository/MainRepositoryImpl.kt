package com.example.taskmanagement.data.repository

import com.example.taskmanagement.data.data_source.RemoteDataSource
import com.example.taskmanagement.domain.data_models.utils.Credentials
import com.example.taskmanagement.domain.data_models.utils.RegisterUser
import com.example.taskmanagement.domain.data_models.utils.UserStatus
import com.example.taskmanagement.domain.repository.MainRepository

class MainRepositoryImpl(private val remote: RemoteDataSource) : MainRepository {
    private lateinit var userStatus: UserStatus
    override suspend fun registerUser(registerUser: RegisterUser): UserStatus {
        userStatus = remote.registerUser(registerUser)
        return userStatus
    }

    override suspend fun loginUser(credentials: Credentials): UserStatus {
        userStatus = remote.loginUser(credentials)
        return userStatus
    }

    override suspend fun logoutUser(): UserStatus {
        userStatus = UserStatus.LoggedOut
        return userStatus
    }
}