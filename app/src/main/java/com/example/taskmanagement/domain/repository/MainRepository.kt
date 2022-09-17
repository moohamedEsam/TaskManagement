package com.example.taskmanagement.domain.repository

import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import com.example.taskmanagement.domain.dataModels.project.Project
import com.example.taskmanagement.domain.dataModels.task.Task
import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.*
import com.example.taskmanagement.domain.dataModels.project.ProjectView
import com.example.taskmanagement.domain.dataModels.task.Comment
import com.example.taskmanagement.domain.dataModels.task.TaskItem
import com.example.taskmanagement.domain.dataModels.task.TaskView
import com.example.taskmanagement.domain.dataModels.team.Invitation
import com.example.taskmanagement.domain.dataModels.team.TeamDto
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.dataModels.user.Dashboard
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MainRepository {
    suspend fun registerUser(userProfile: SignUpUserBody): Resource<Token>
    suspend fun loginUser(credentials: Credentials): UserStatus
    suspend fun logoutUser(): UserStatus
    suspend fun setUserStatus(status: UserStatus)
    suspend fun observeUser(): SharedFlow<UserStatus>
    suspend fun searchMembers(query: String): Resource<List<User>>
    suspend fun getCurrentUserDashboard(): Resource<Dashboard>
    //suspend fun updateUser(user: User): Resource<User>
    suspend fun getCurrentUserTasks(): Resource<List<Task>>
    suspend fun getTask(taskId: String): Resource<TaskView>
    suspend fun createTask(task: Task): Resource<Task>
    suspend fun updateTask(task: Task): Resource<Task>
    suspend fun updateTaskStatus(taskId: String): Resource<Boolean>
    suspend fun updateTaskItems(taskItems: List<String>): Resource<List<TaskItem>>
    suspend fun createTaskItems(taskItems: List<TaskItem>): Resource<List<TaskItem>>
    suspend fun deleteTaskItem(taskItem: String): Resource<Boolean>
    suspend fun createComments(comments: List<Comment>): Resource<List<Comment>>
    suspend fun updateComment(comment: Comment): Resource<Comment>
    suspend fun deleteComment(commentId: String): Resource<Boolean>

    //suspend fun getUser(context: Context): Resource<User>
    suspend fun getCurrentUserProfile(): Resource<User>

    suspend fun getCurrentUserProjects(): Resource<List<Project>>
    suspend fun getProject(projectId: String): Resource<ProjectView>
    suspend fun getTeam(teamId: String): Resource<TeamView>
    suspend fun createProject(project: Project): Resource<Project>
    suspend fun updateProject(project: Project): Resource<Project>

    suspend fun getCurrentUserTeams(): Resource<List<Team>>
    suspend fun updateTeam(team: Team): Resource<TeamDto>
    suspend fun createTeam(team: Team): Resource<TeamDto>
    suspend fun sendInvitations(teamId: String, invitation: List<String>): Resource<Boolean>
    suspend fun createTag(tag: Tag, parentRoute: ParentRoute): Resource<Tag>
    suspend fun assignTag(
        id: String,
        parentRoute: ParentRoute,
        members: List<ActiveUser>
    ): Resource<List<ActiveUser>>

    suspend fun getCurrentUserTag(parentRoute: ParentRoute, id: String): Resource<Tag>
    suspend fun removeMembers(
        parentRoute: ParentRoute,
        id: String,
        members: List<String>
    ): Resource<Boolean>
}