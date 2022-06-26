package com.example.taskmanagement.domain.repository

import android.content.Context
import com.example.taskmanagement.domain.dataModels.Project
import com.example.taskmanagement.domain.dataModels.Task
import com.example.taskmanagement.domain.dataModels.Team
import com.example.taskmanagement.domain.dataModels.User
import com.example.taskmanagement.domain.dataModels.utils.Credentials
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.SignUpUser
import com.example.taskmanagement.domain.dataModels.utils.UserStatus
import com.example.taskmanagement.domain.dataModels.views.ProjectView
import com.example.taskmanagement.domain.dataModels.views.TaskView
import com.example.taskmanagement.domain.dataModels.views.TeamView
import com.example.taskmanagement.domain.dataModels.views.UserView

interface IMainRepository {
    suspend fun registerUser(userProfile: SignUpUser, context: Context): UserStatus
    suspend fun loginUser(credentials: Credentials, context: Context): UserStatus
    suspend fun logoutUser(): UserStatus

    //suspend fun updateUser(user: User): Resource<User>
    suspend fun getUserTasks(): Resource<List<Task>>
    suspend fun getTask(taskId: String): Resource<TaskView>
    suspend fun saveTask(task: Task): Resource<Task>

    //suspend fun getUser(context: Context): Resource<User>
    suspend fun getUserProfile(): Resource<User>

    suspend fun getUserProjects(): Resource<List<Project>>
    suspend fun getProject(projectId: String): Resource<ProjectView>
    suspend fun getUserTeam(teamId: String): Resource<TeamView>
    suspend fun saveProject(project: Project): Resource<Project>
    suspend fun getUserTeams(): Resource<List<Team>>
}