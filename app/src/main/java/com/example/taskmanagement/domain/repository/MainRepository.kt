package com.example.taskmanagement.domain.repository

import com.example.taskmanagement.domain.data_models.utils.Credentials
import com.example.taskmanagement.domain.data_models.utils.RegisterUser
import com.example.taskmanagement.domain.data_models.utils.UserStatus

interface MainRepository {
    suspend fun registerUser(registerUser: RegisterUser): UserStatus
    suspend fun loginUser(credentials: Credentials): UserStatus
    suspend fun logoutUser(): UserStatus


}