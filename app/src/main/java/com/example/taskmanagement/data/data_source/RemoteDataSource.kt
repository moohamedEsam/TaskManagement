package com.example.taskmanagement.data.data_source

import com.example.taskmanagement.domain.data_models.*
import com.example.taskmanagement.domain.data_models.utils.Credentials
import com.example.taskmanagement.domain.data_models.utils.RegisterUser
import com.example.taskmanagement.domain.data_models.utils.Resource
import com.example.taskmanagement.domain.data_models.utils.UserStatus

interface RemoteDataSource {
    suspend fun loginUser(credentials: Credentials): UserStatus
    suspend fun registerUser(registerUser: RegisterUser): UserStatus


    suspend fun getUserTasks(token: Token): Resource<List<Task>>
    suspend fun getUserTask(token: Token, taskId: String): Resource<Task>
    suspend fun createTask(token: Token, task: Task): Resource<Task>
    suspend fun updateTask(token: Token, task: Task): Resource<Task>
    suspend fun deleteTask(token: Token, taskId: String): Resource<ConfirmationResponse>
    suspend fun getUserTasksByStatus(token: Token, status: TaskStatus): Resource<List<Task>>
    suspend fun getUserTasksByPriority(token: Token, priority: TaskPriority): Resource<List<Task>>
    suspend fun createTaskComment(token: Token, taskId: String, comment: Comment): Resource<Task>
    suspend fun deleteTaskComment(token: Token, taskId: String, commentId: String): Resource<Task>
    suspend fun updateTaskComment(token: Token, taskId: String, comment: Comment): Resource<Task>


    suspend fun getUserProjects(token: Token): Resource<List<Project>>
    suspend fun getUserProject(token: Token, projectId: String): Resource<Project>
    suspend fun createProject(token: Token, project: Project): Resource<Project>
    suspend fun updateProject(token: Token, project: Project): Resource<Project>
    suspend fun deleteProject(token: Token, projectId: String): Resource<ConfirmationResponse>

    suspend fun getUserTeams(token: Token): Resource<List<Team>>
    suspend fun getUserTeam(token: Token, teamId: String): Resource<Team>
    suspend fun createTeam(token: Token, team: Team): Resource<Team>
    suspend fun updateTeam(token: Token, team: Team): Resource<Team>
    suspend fun deleteTeam(token: Token, teamId: String): Resource<Team>


}