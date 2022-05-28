package com.example.taskmanagement.data.repository

import android.content.Context
import com.example.taskmanagement.data.data_source.RemoteDataSource
import com.example.taskmanagement.domain.data_models.Task
import com.example.taskmanagement.domain.data_models.Token
import com.example.taskmanagement.domain.data_models.User
import com.example.taskmanagement.domain.data_models.utils.Credentials
import com.example.taskmanagement.domain.data_models.utils.Resource
import com.example.taskmanagement.domain.data_models.utils.UserProfile
import com.example.taskmanagement.domain.data_models.utils.UserStatus
import com.example.taskmanagement.domain.repository.MainRepository
import java.util.*

class MainRepositoryImpl(private val remote: RemoteDataSource) : MainRepository {
    private lateinit var userStatus: UserStatus
    override suspend fun registerUser(userProfile: UserProfile, context: Context): UserStatus {
        userStatus = remote.registerUser(userProfile, context)
        return userStatus
    }

    override suspend fun getUserTasks(): Resource<List<Task>> {
        return remote.getUserTasks()
    }

    override fun isUserStillLoggedIn(context: Context): Boolean {
        return remote.isUserStillLoggedIn(context)
    }

    override suspend fun loginUser(credentials: Credentials, context: Context): UserStatus {
        userStatus = remote.loginUser(credentials, context)
        return userStatus
    }

    override suspend fun logoutUser(): UserStatus {
        userStatus = UserStatus.LoggedOut
        return userStatus
    }
}