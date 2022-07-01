package com.example.taskmanagement.data.data_source

import android.content.Context
import com.example.taskmanagement.domain.dataModels.*
import com.example.taskmanagement.domain.dataModels.utils.*
import com.example.taskmanagement.domain.dataModels.views.ProjectView
import com.example.taskmanagement.domain.dataModels.views.TaskView
import com.example.taskmanagement.domain.dataModels.views.TeamView
import com.example.taskmanagement.domain.dataModels.views.UserView
import java.util.*

interface IRemoteDataSource {
    suspend fun loginUser(credentials: Credentials, context: Context): UserStatus
    suspend fun registerUser(userProfile: SignUpUser, context: Context): UserStatus


    suspend fun getUserTasks(): Resource<List<Task>>
    suspend fun getUserTask(taskId: String): Resource<TaskView>
    suspend fun createTask(task: Task): Resource<Task>
    suspend fun updateTask(task: Task): Resource<Task>
    suspend fun deleteTask(taskId: String): Resource<ConfirmationResponse>
    suspend fun getUserTasksByStatus(status: TaskStatus): Resource<List<Task>>
    suspend fun getUserTasksByPriority(priority: TaskPriority): Resource<List<Task>>
    suspend fun createTaskComment(taskId: String, comment: Comment): Resource<Task>
    suspend fun deleteTaskComment(taskId: String, commentId: UUID): Resource<Task>
    suspend fun updateTaskComment(taskId: String, comment: Comment): Resource<Task>


    suspend fun getUserProjects(): Resource<List<Project>>
    suspend fun getUserProject(projectId: String): Resource<Project>
    suspend fun createProject(project: Project): Resource<Project>
    suspend fun updateProject(project: Project): Resource<Project>
    suspend fun deleteProject(projectId: String): Resource<ConfirmationResponse>

    suspend fun getUserTeams(): Resource<List<Team>>
    suspend fun getUserTeam(teamId: String): Resource<TeamView>
    suspend fun createTeam(team: CreateTeamBody): Resource<TeamView>
    suspend fun updateTeam(team: CreateTeamBody): Resource<TeamView>
    suspend fun deleteTeam(teamId: String): Resource<Team>
    suspend fun getUserProfile(): Resource<User>
    suspend fun getProject(projectId: String): Resource<ProjectView>
    suspend fun saveTask(task: Task): Resource<Task>
    suspend fun saveProject(project: Project): Resource<Project>


}