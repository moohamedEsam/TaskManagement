package com.example.taskmanagement.data.repository

import android.content.Context
import com.example.taskmanagement.data.data_source.IRemoteDataSource
import com.example.taskmanagement.domain.dataModels.Project
import com.example.taskmanagement.domain.dataModels.Task
import com.example.taskmanagement.domain.dataModels.Team
import com.example.taskmanagement.domain.dataModels.User
import com.example.taskmanagement.domain.dataModels.utils.*
import com.example.taskmanagement.domain.dataModels.views.ProjectView
import com.example.taskmanagement.domain.dataModels.views.TaskView
import com.example.taskmanagement.domain.dataModels.views.TeamView
import com.example.taskmanagement.domain.repository.IMainRepository

class MainRepositoryImpl(private val remote: IRemoteDataSource) : IMainRepository {
    private lateinit var userStatus: UserStatus
    override suspend fun registerUser(userProfile: SignUpUserBody, context: Context): UserStatus {
        userStatus = remote.registerUser(userProfile, context)
        return userStatus
    }

    override suspend fun getUserTasks(): Resource<List<Task>> {
        return remote.getUserTasks()
    }

    override suspend fun updateTeam(team: CreateTeamBody): Resource<Team> {
        return remote.updateTeam(team)
    }

    override suspend fun createTeam(team: CreateTeamBody): Resource<Team> {
        return remote.createTeam(team)
    }

    override suspend fun getUserTeams(): Resource<List<Team>> {
        return remote.getUserTeams()
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

    override suspend fun getUserProfile(): Resource<User> {
        return remote.getUserProfile()
    }

    override suspend fun getUserProjects(): Resource<List<Project>> {
        return remote.getUserProjects()
    }

    override suspend fun getProject(projectId: String): Resource<ProjectView> {
        return remote.getUserProject(projectId)
    }

    override suspend fun saveTask(task: Task): Resource<Task> {
        return remote.createTask(task)
    }

    override suspend fun getUserTeam(teamId: String): Resource<TeamView> {
        return remote.getUserTeam(teamId)
    }

    override suspend fun saveProject(project: Project): Resource<Project> {
        return remote.createProject(project)
    }
}