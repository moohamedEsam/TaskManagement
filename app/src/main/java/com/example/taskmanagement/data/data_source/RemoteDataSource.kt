package com.example.taskmanagement.data.data_source

import android.content.Context
import com.example.taskmanagement.domain.data_models.*
import com.example.taskmanagement.domain.data_models.utils.Credentials
import com.example.taskmanagement.domain.data_models.utils.UserProfile
import com.example.taskmanagement.domain.data_models.utils.Resource
import com.example.taskmanagement.domain.data_models.utils.UserStatus

interface RemoteDataSource {
    suspend fun loginUser(credentials: Credentials, context: Context): UserStatus
    suspend fun registerUser(userProfile: UserProfile, context: Context): UserStatus


    suspend fun getUserTasks(): Resource<List<Task>>
    suspend fun getUserTask(taskId: String): Resource<Task>
    suspend fun createTask(task: Task): Resource<Task>
    suspend fun updateTask(task: Task): Resource<Task>
    suspend fun deleteTask(taskId: String): Resource<ConfirmationResponse>
    suspend fun getUserTasksByStatus(status: TaskStatus): Resource<List<Task>>
    suspend fun getUserTasksByPriority(priority: TaskPriority): Resource<List<Task>>
    suspend fun createTaskComment(taskId: String, comment: Comment): Resource<Task>
    suspend fun deleteTaskComment(taskId: String, commentId: String): Resource<Task>
    suspend fun updateTaskComment(taskId: String, comment: Comment): Resource<Task>


    suspend fun getUserProjects(): Resource<List<Project>>
    suspend fun getUserProject(projectId: String): Resource<Project>
    suspend fun createProject(project: Project): Resource<Project>
    suspend fun updateProject(project: Project): Resource<Project>
    suspend fun deleteProject(projectId: String): Resource<ConfirmationResponse>

    suspend fun getUserTeams(): Resource<List<Team>>
    suspend fun getUserTeam(teamId: String): Resource<Team>
    suspend fun createTeam(team: Team): Resource<Team>
    suspend fun updateTeam(team: Team): Resource<Team>
    suspend fun deleteTeam(teamId: String): Resource<Team>
    fun isUserStillLoggedIn(context: Context): Boolean


}