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
import com.example.taskmanagement.domain.dataModels.task.Comment
import com.example.taskmanagement.domain.dataModels.task.TaskItem
import com.example.taskmanagement.domain.dataModels.task.TaskView
import com.example.taskmanagement.domain.dataModels.team.TeamDto
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.repository.MainRepository

class MainRepositoryImpl(private val remote: RemoteDataSource) : MainRepository {
    private lateinit var userStatus: UserStatus
    private suspend fun <T> baseMapApiToResource(delegate: suspend () -> T): Resource<T> {
        return try {
            val result = delegate()
            Resource.Success(result)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun deleteTaskItem(
        taskItem: String,
        taskId: String
    ): Resource<Boolean> = baseMapApiToResource { remote.deleteTaskItem(taskId, taskItem) }

    override suspend fun createTaskItems(
        taskItems: List<TaskItem>,
        taskId: String
    ): Resource<List<TaskItem>> = baseMapApiToResource { remote.createTaskItems(taskId, taskItems) }

    override suspend fun registerUser(userProfile: SignUpUserBody): Resource<Token> =
        baseMapApiToResource { remote.registerUser(userProfile) }

    override suspend fun getCurrentUserTasks(): Resource<List<Task>> =
        baseMapApiToResource(remote::getCurrentUserTasks)

    override suspend fun updateTeam(team: Team): Resource<TeamDto> =
        baseMapApiToResource { remote.updateTeam(team) }

    override suspend fun createTag(tag: Tag, parentRoute: ParentRoute): Resource<Tag> =
        baseMapApiToResource { remote.createTag(tag, parentRoute) }

    override suspend fun assignTag(
        id: String,
        parentRoute: ParentRoute,
        members: List<ActiveUser>
    ): Resource<List<ActiveUser>> =
        baseMapApiToResource { remote.assignTag(id, parentRoute, members) }

    override suspend fun updateTask(task: Task): Resource<Task> =
        baseMapApiToResource { remote.updateTask(task) }

    override suspend fun updateTaskStatus(taskId: String): Resource<Boolean> =
        baseMapApiToResource { remote.updateTaskStatus(taskId) }

    override suspend fun getCurrentUserTag(parentRoute: ParentRoute, id: String): Resource<Tag> =
        baseMapApiToResource { remote.getCurrentUserTag(parentRoute, id) }

    override suspend fun updateComment(comment: Comment): Resource<Comment> =
        baseMapApiToResource { remote.updateTaskComment(comment) }

    override suspend fun deleteComment(commentId: String): Resource<Boolean> =
        baseMapApiToResource { remote.deleteTaskComment(commentId) }

    override suspend fun updateProject(project: Project): Resource<Project> =
        baseMapApiToResource { remote.updateProject(project) }

    override suspend fun createTeam(team: Team): Resource<TeamDto> =
        baseMapApiToResource { remote.createTeam(team) }

    override suspend fun sendInvitations(
        teamId: String,
        invitation: List<String>
    ): Resource<Boolean> = baseMapApiToResource { remote.sendInvitation(teamId, invitation) }

    override suspend fun searchMembers(query: String): Resource<List<User>> =
        baseMapApiToResource { remote.searchMembers(query) }

    override suspend fun getCurrentUserTeams(): Resource<List<Team>> =
        baseMapApiToResource(remote::getCurrentUserTeams)


    override suspend fun getTask(taskId: String): Resource<TaskView> =
        baseMapApiToResource { remote.getTask(taskId) }

    override suspend fun loginUser(credentials: Credentials): UserStatus {
        return try {
            val token = remote.loginUser(credentials)
            userStatus = UserStatus.Authorized(token)
            userStatus
        } catch (exception: Exception) {
            userStatus = UserStatus.Forbidden(exception.message)
            userStatus
        }
    }

    override suspend fun logoutUser(): UserStatus {
        userStatus = UserStatus.LoggedOut
        return userStatus
    }

    override suspend fun updateTaskItems(
        taskItems: List<String>,
        taskId: String
    ): Resource<List<TaskItem>> =
        baseMapApiToResource { remote.updateTaskItems(taskId, taskItems) }

    override suspend fun removeMembers(
        parentRoute: ParentRoute,
        id: String,
        members: List<String>
    ): Resource<Boolean> = baseMapApiToResource { remote.removeMembers(id, parentRoute, members) }

    override suspend fun createComments(comments: List<Comment>): Resource<List<Comment>> =
        baseMapApiToResource { remote.createComments(comments) }

    override suspend fun getCurrentUserProfile(): Resource<User> =
        baseMapApiToResource(remote::getCurrentUserProfile)

    override suspend fun getCurrentUserProjects(): Resource<List<Project>> =
        baseMapApiToResource(remote::getCurrentUserProjects)

    override suspend fun getProject(projectId: String): Resource<ProjectView> =
        baseMapApiToResource { remote.getProject(projectId) }

    override suspend fun createTask(task: Task): Resource<Task> =
        baseMapApiToResource { remote.createTask(task) }

    override suspend fun getTeam(teamId: String): Resource<TeamView> =
        baseMapApiToResource { remote.getTeam(teamId) }

    override suspend fun createProject(project: Project): Resource<Project> =
        baseMapApiToResource { remote.createProject(project) }
}