package com.example.taskmanagement.data.data_source

import android.content.Context
import com.example.taskmanagement.domain.dataModels.*
import com.example.taskmanagement.domain.dataModels.utils.*
import com.example.taskmanagement.domain.dataModels.views.ProjectView
import com.example.taskmanagement.domain.dataModels.views.TaskView
import com.example.taskmanagement.domain.dataModels.views.TeamView
import java.util.*

interface IRemoteDataSource {
    suspend fun loginUser(credentials: Credentials, context: Context): UserStatus
    suspend fun registerUser(user: SignUpUserBody, context: Context): UserStatus


    suspend fun getUserTasks(): Resource<List<Task>>
    suspend fun getUserTask(taskId: String): Resource<TaskView>
    suspend fun createTask(task: Task): Resource<Task>
    suspend fun updateTask(task: Task): Resource<Task>
    suspend fun deleteTask(taskId: String): Resource<ConfirmationResponse>
    suspend fun getUserTasksByStatus(status: TaskStatus): Resource<List<Task>>
    suspend fun getUserTasksByPriority(priority: TaskPriority): Resource<List<Task>>
    suspend fun createTaskComment(comment: Comment): Resource<Comment>
    suspend fun deleteTaskComment(commentId: String): Resource<ConfirmationResponse>
    suspend fun updateTaskComment(comment: Comment): Resource<Comment>


    suspend fun getUserProjects(): Resource<List<Project>>
    suspend fun getUserProject(projectId: String): Resource<ProjectView>
    suspend fun createProject(project: Project): Resource<Project>
    suspend fun updateProject(project: Project): Resource<Project>
    suspend fun deleteProject(projectId: String): Resource<ConfirmationResponse>

    suspend fun getUserTeams(): Resource<List<Team>>
    suspend fun getUserTeam(teamId: String): Resource<TeamView>
    suspend fun createTeam(team: CreateTeamBody): Resource<Team>
    suspend fun updateTeam(team: CreateTeamBody): Resource<Team>
    suspend fun deleteTeam(teamId: String): Resource<ConfirmationResponse>
    suspend fun getUserProfile(): Resource<User>

}