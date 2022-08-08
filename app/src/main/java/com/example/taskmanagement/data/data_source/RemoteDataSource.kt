package com.example.taskmanagement.data.data_source

import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import com.example.taskmanagement.domain.dataModels.project.Project
import com.example.taskmanagement.domain.dataModels.task.*
import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.*
import com.example.taskmanagement.domain.dataModels.project.ProjectView
import com.example.taskmanagement.domain.dataModels.task.TaskView
import com.example.taskmanagement.domain.dataModels.team.TeamDto
import com.example.taskmanagement.domain.dataModels.team.TeamView

interface RemoteDataSource {
    suspend fun loginUser(credentials: Credentials): UserStatus
    suspend fun registerUser(user: SignUpUserBody): Resource<Token>
    suspend fun searchMembers(query: String): Resource<List<User>>
    suspend fun getUserTag(parentRoute: String, id: String): Resource<Tag>
    suspend fun createTag(tag: Tag): Resource<Tag>

    suspend fun getUserTasks(): Resource<List<Task>>
    suspend fun getUserTask(taskId: String): Resource<TaskView>
    suspend fun createTask(task: Task): Resource<Task>
    suspend fun updateTask(task: Task): Resource<Task>
    suspend fun deleteTask(taskId: String): Resource<ConfirmationResponse>
    suspend fun getUserTasksByStatus(status: TaskStatus): Resource<List<Task>>
    suspend fun getUserTasksByPriority(priority: Priority): Resource<List<Task>>
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
    suspend fun createTeam(team: Team): Resource<TeamDto>
    suspend fun updateTeam(team: Team): Resource<TeamDto>
    suspend fun deleteTeam(teamId: String): Resource<ConfirmationResponse>
    suspend fun getUserProfile(): Resource<User>
    suspend fun assignTag(teamId: String, members: List<ActiveUser>): Resource<List<ActiveUser>>

}