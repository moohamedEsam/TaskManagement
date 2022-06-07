package com.example.taskmanagement.data.repository

import android.content.Context
import com.example.taskmanagement.data.data_source.RemoteDataSource
import com.example.taskmanagement.domain.dataModels.*
import com.example.taskmanagement.domain.dataModels.utils.Credentials
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.UserProfile
import com.example.taskmanagement.domain.dataModels.utils.UserStatus
import com.example.taskmanagement.domain.dataModels.views.ProjectView
import com.example.taskmanagement.domain.dataModels.views.TaskView
import com.example.taskmanagement.domain.dataModels.views.TeamView
import com.example.taskmanagement.domain.dataModels.views.UserView
import com.example.taskmanagement.domain.repository.MainRepository

class MainRepositoryImpl(private val remote: RemoteDataSource) : MainRepository {
    private lateinit var userStatus: UserStatus
    override suspend fun registerUser(userProfile: UserProfile, context: Context): UserStatus {
        userStatus = remote.registerUser(userProfile, context)
        return userStatus
    }

    override suspend fun getUserTasks(): Resource<List<Task>> {
        return remote.getUserTasks()
    }


    override suspend fun getTask(taskId: String): Resource<TaskView> {
        return remote.getUserTask(taskId)
    }

    override suspend fun loginUser(credentials: Credentials, context: Context): UserStatus {
        userStatus = remote.loginUser(credentials, context)
        return userStatus
    }

    override suspend fun logoutUser(): UserStatus {
        userStatus = UserStatus.LoggedOut
        return userStatus
    }

    override suspend fun getUserProfile(): Resource<UserView> {
        return remote.getUserProfile()
    }

    override suspend fun getUserProjects(): Resource<List<Project>> {
        return remote.getUserProjects()
    }

    override suspend fun getProject(projectId: String): Resource<ProjectView> {
        return remote.getProject(projectId)
    }

    override suspend fun saveTask(task: Task): Resource<Task> {
        return remote.saveTask(task)
    }

    override suspend fun getUserTeam(teamId: String): Resource<TeamView> {
        return remote.getUserTeam(teamId)
    }

    override suspend fun saveProject(project: Project): Resource<Project> {
        return remote.saveProject(project)
    }
}