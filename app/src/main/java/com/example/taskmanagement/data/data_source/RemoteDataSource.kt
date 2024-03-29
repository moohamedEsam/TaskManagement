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
import com.example.taskmanagement.domain.dataModels.team.Invitation
import com.example.taskmanagement.domain.dataModels.team.TeamDto
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.dataModels.user.Dashboard

interface RemoteDataSource {
    suspend fun loginUser(credentials: Credentials): Token
    suspend fun registerUser(user: SignUpUserBody): Token
    suspend fun logoutUser()
    suspend fun searchMembers(query: String): List<User>
    suspend fun getCurrentUserProfile(): User
    suspend fun getUserProfile(userId: String): User
    suspend fun acceptInvitation(invitationId: String): Boolean
    suspend fun rejectInvitation(invitationId: String): Boolean
    suspend fun getUserInvitations(): List<Invitation>
    suspend fun getCurrentUserTag(parentRoute: ParentRoute, id: String): Tag

    suspend fun createTag(tag: Tag, parentRoute: ParentRoute): Tag
    suspend fun getCurrentUserDashboard(): Dashboard
    suspend fun getCurrentUserTasks(): List<Task>
    suspend fun getTask(taskId: String): TaskView
    suspend fun createTask(task: Task): Task
    suspend fun updateTask(task: Task): Task
    suspend fun updateTaskStatus(taskId: String): Boolean
    suspend fun deleteTask(taskId: String): Boolean
    suspend fun getUserTasksByStatus(status: TaskStatus): List<Task>
    suspend fun getUserTasksByPriority(priority: Priority): List<Task>
    suspend fun createComments(comments: List<Comment>): List<Comment>
    suspend fun deleteTaskComment(commentId: String): Boolean
    suspend fun updateTaskComment(comment: Comment): Comment

    suspend fun createTaskItems(taskItems: List<TaskItem>): List<TaskItem>
    suspend fun updateTaskItems(taskItems: List<String>): List<TaskItem>


    suspend fun deleteTaskItem(taskItem: String): Boolean
    suspend fun getCurrentUserProjects(): List<Project>
    suspend fun getProject(projectId: String): ProjectView
    suspend fun createProject(project: Project): Project
    suspend fun updateProject(project: Project): Project

    suspend fun deleteProject(projectId: String): Boolean
    suspend fun getCurrentUserTeams(): List<Team>
    suspend fun getTeam(teamId: String): TeamView
    suspend fun createTeam(team: Team): TeamDto
    suspend fun updateTeam(team: Team): TeamDto
    suspend fun deleteTeam(teamId: String): Boolean
    suspend fun sendInvitation(teamId: String, userIds: List<String>): Boolean
    suspend fun assignTag(
        id: String,
        parentRoute: ParentRoute,
        members: List<ActiveUser>
    ): List<ActiveUser>

    suspend fun removeMembers(
        id: String,
        parentRoute: ParentRoute,
        members: List<String>
    ): Boolean


}