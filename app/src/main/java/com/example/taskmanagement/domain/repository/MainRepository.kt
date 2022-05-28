package com.example.taskmanagement.domain.repository

import android.content.Context
import com.example.taskmanagement.domain.data_models.Task
import com.example.taskmanagement.domain.data_models.User
import com.example.taskmanagement.domain.data_models.utils.Credentials
import com.example.taskmanagement.domain.data_models.utils.Resource
import com.example.taskmanagement.domain.data_models.utils.UserProfile
import com.example.taskmanagement.domain.data_models.utils.UserStatus

interface MainRepository {
    suspend fun registerUser(userProfile: UserProfile, context: Context): UserStatus
    suspend fun loginUser(credentials: Credentials, context: Context): UserStatus
    suspend fun logoutUser(): UserStatus
    //suspend fun updateUser(user: User): Resource<User>
    suspend fun getUserTasks(): Resource<List<Task>>
    fun isUserStillLoggedIn(context: Context): Boolean

    //suspend fun getUser(context: Context): Resource<User>
}