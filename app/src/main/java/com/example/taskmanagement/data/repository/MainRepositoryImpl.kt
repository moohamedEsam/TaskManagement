package com.example.taskmanagement.data.repository

import com.example.taskmanagement.data.data_source.RemoteDataSource
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
import com.example.taskmanagement.domain.repository.MainRepository

class MainRepositoryImpl(private val remote: RemoteDataSource) : MainRepository {
    private lateinit var userStatus: UserStatus
    override suspend fun registerUser(userProfile: SignUpUserBody): Resource<Token> {
        val result = remote.registerUser(userProfile)
        userStatus = if (result is Resource.Success && result.data != null)
            UserStatus.Authorized(result.data)
        else
            UserStatus.Forbidden(result.message)
        return result
    }

    override suspend fun getUserTasks(): Resource<List<Task>> {
        return remote.getUserTasks()
    }

    override suspend fun updateTeam(team: Team): Resource<TeamDto> {
        return remote.updateTeam(team)
    }

    override suspend fun createTag(tag: Tag, parentRoute: ParentRoute): Resource<Tag> {
        return remote.createTag(tag, parentRoute)
    }

    override suspend fun assignTag(
        id: String,
        parentRoute: ParentRoute,
        members: List<ActiveUser>
    ): Resource<List<ActiveUser>> {
        return remote.assignTag(id, parentRoute, members)
    }

    override suspend fun updateTask(task: Task): Resource<Task> {
        return remote.updateTask(task)
    }

    override suspend fun getUserTag(parentRoute: String, id: String): Resource<Tag> {
        return remote.getUserTag(parentRoute, id)
    }

    override suspend fun updateProject(project: Project): Resource<Project> {
        return remote.updateProject(project)
    }

    override suspend fun createTeam(team: Team): Resource<TeamDto> {
        return remote.createTeam(team)
    }

    override suspend fun searchMembers(query: String): Resource<List<User>> {
        return remote.searchMembers(query)
    }

    override suspend fun getUserTeams(): Resource<List<Team>> {
        return remote.getUserTeams()
    }


    override suspend fun getTask(taskId: String): Resource<TaskView> {
        return remote.getUserTask(taskId)
    }

    override suspend fun loginUser(credentials: Credentials): UserStatus {
        userStatus = remote.loginUser(credentials)
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