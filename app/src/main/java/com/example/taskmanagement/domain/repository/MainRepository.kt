package com.example.taskmanagement.domain.repository

import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import com.example.taskmanagement.domain.dataModels.project.Project
import com.example.taskmanagement.domain.dataModels.task.Task
import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.*
import com.example.taskmanagement.domain.dataModels.project.ProjectView
import com.example.taskmanagement.domain.dataModels.task.TaskView
import com.example.taskmanagement.domain.dataModels.team.TeamDto
import com.example.taskmanagement.domain.dataModels.team.TeamView

interface MainRepository {
    suspend fun registerUser(userProfile: SignUpUserBody): Resource<Token>
    suspend fun loginUser(credentials: Credentials): UserStatus
    suspend fun logoutUser(): UserStatus
    suspend fun searchMembers(query: String): Resource<List<User>>

    //suspend fun updateUser(user: User): Resource<User>
    suspend fun getUserTasks(): Resource<List<Task>>
    suspend fun getTask(taskId: String): Resource<TaskView>
    suspend fun saveTask(task: Task): Resource<Task>
    suspend fun updateTask(task: Task): Resource<Task>

    //suspend fun getUser(context: Context): Resource<User>
    suspend fun getUserProfile(): Resource<User>

    suspend fun getUserProjects(): Resource<List<Project>>
    suspend fun getProject(projectId: String): Resource<ProjectView>
    suspend fun getUserTeam(teamId: String): Resource<TeamView>
    suspend fun saveProject(project: Project): Resource<Project>
    suspend fun updateProject(project: Project): Resource<Project>

    suspend fun getUserTeams(): Resource<List<Team>>
    suspend fun updateTeam(team: Team): Resource<TeamDto>
    suspend fun createTeam(team: Team): Resource<TeamDto>
    suspend fun createTag(tag: Tag, parentRoute: ParentRoute): Resource<Tag>
    suspend fun assignTag(
        id: String,
        parentRoute: ParentRoute,
        members: List<ActiveUser>
    ): Resource<List<ActiveUser>>

    suspend fun getUserTag(parentRoute: String, id: String): Resource<Tag>
}