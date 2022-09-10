package com.example.taskmanagement.data.data_source_impl

import com.example.taskmanagement.data.data_source.RemoteDataSource
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import com.example.taskmanagement.domain.dataModels.project.Project
import com.example.taskmanagement.domain.dataModels.project.ProjectView
import com.example.taskmanagement.domain.dataModels.task.*
import com.example.taskmanagement.domain.dataModels.team.Invitation
import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.domain.dataModels.team.TeamDto
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.*
import com.example.taskmanagement.domain.utils.Urls
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.io.File

class RemoteDataSourceImpl(private val client: HttpClient) : RemoteDataSource {
    override suspend fun loginUser(credentials: Credentials): Token {
        val response = client.post(Urls.SIGN_IN) {
            setBody(credentials)
        }
        return getResponseResult(response)
    }

    override suspend fun getCurrentUserTag(parentRoute: ParentRoute, id: String): Tag {
        val response = client.get(Urls.getUserTag(parentRoute, id))
        return getResponseResult(response)
    }

    override suspend fun getCurrentUserProfile(): User {
        val response = client.get(Urls.PROFILE)
        return getResponseResult(response)
    }


    override suspend fun getCurrentUserTasks(): List<Task> {
        val response = client.get(Urls.TASKS)
        return getResponseResult(response)
    }

    override suspend fun getTask(taskId: String): TaskView {
        val response = client.get(Urls.getTaskUrl(taskId))
        return getResponseResult(response)
    }

    override suspend fun createTask(task: Task): Task {
        val response = client.post(Urls.TASKS) {
            setBody(task)
        }
        return getResponseResult(response)
    }

    override suspend fun updateTask(task: Task): Task {
        val response = client.put(Urls.TASKS) {
            setBody(task)
        }
        return getResponseResult(response)
    }

    override suspend fun acceptInvitation(invitationId: String): Boolean {
        val response = client.post(Urls.getAcceptInvitationUrl(invitationId))
        return getResponseResult(response)
    }

    override suspend fun rejectInvitation(invitationId: String): Boolean {
        val response = client.post(Urls.getRejectInvitationUrl(invitationId))
        return getResponseResult(response)
    }

    override suspend fun getUserInvitations(): List<Invitation> {
        val response = client.get(Urls.INVITATION)
        return getResponseResult(response)
    }

    override suspend fun createTaskItems(
        taskId: String,
        taskItems: List<TaskItem>
    ): List<TaskItem> {
        val response = client.post(Urls.getTaskItemRoute(taskId)) {
            setBody(taskItems)
        }
        return getResponseResult(response)
    }

    override suspend fun updateTaskItems(
        taskId: String,
        taskItems: List<String>
    ): List<TaskItem> {
        val response = client.put(Urls.getTaskItemRoute(taskId)) {
            setBody(taskItems)
        }
        return getResponseResult(response)
    }

    override suspend fun deleteTaskItem(taskId: String, taskItem: String): Boolean {
        val response = client.delete(Urls.getDeleteTaskItemRoute(taskId, taskItem))
        return getResponseResult(response)
    }

    override suspend fun sendInvitation(teamId: String, userIds: List<String>): Boolean {
        val response = client.post(Urls.getTeamUrl(teamId)) {
            setBody(userIds)
        }
        return getResponseResult(response)
    }


    override suspend fun deleteTask(taskId: String): Boolean {
        val response = client.delete(Urls.getTaskUrl(taskId))
        return getResponseResult(response)
    }

    override suspend fun updateTaskStatus(taskId: String): Boolean {
        val response = client.put(Urls.getTaskUrlStatus(taskId))
        return getResponseResult(response)
    }

    override suspend fun getUserTasksByStatus(
        status: TaskStatus
    ): List<Task> {
        val response = client.get(Urls.getTasksByStatusUrl(status))
        return getResponseResult(response)
    }

    override suspend fun getUserTasksByPriority(
        priority: Priority
    ): List<Task> {
        val response = client.get(Urls.getTasksByPriorityUrl(priority))
        return getResponseResult(response)
    }

    override suspend fun createComments(
        comments: List<Comment>
    ): List<Comment> {
        val response = client.post(Urls.COMMENTS) {
            setBody(comments)
        }
        return getResponseResult(response)
    }

    override suspend fun deleteTaskComment(
        commentId: String
    ): Boolean {

        val response = client.delete(Urls.COMMENTS)
        return getResponseResult(response)
    }

    override suspend fun updateTaskComment(
        comment: Comment
    ): Comment {
        val response = client.put(Urls.COMMENTS) {
            setBody(comment)
        }
        return getResponseResult(response)
    }

    override suspend fun getCurrentUserProjects(): List<Project> {
        val response = client.get(Urls.PROJECTS)
        return getResponseResult(response)
    }

    override suspend fun getProject(projectId: String): ProjectView {
        val response = client.get(Urls.getProjectUrl(projectId))
        return getResponseResult(response)
    }

    override suspend fun createProject(project: Project): Project {

        val response = client.post(Urls.PROJECTS) {
            setBody(project)
        }
        return getResponseResult(response)
    }

    override suspend fun updateProject(project: Project): Project {
        val response = client.put(Urls.PROJECTS) {
            setBody(project)
        }
        return getResponseResult(response)
    }

    override suspend fun deleteProject(
        projectId: String
    ): Boolean {
        val response = client.delete(Urls.getProjectUrl(projectId))
        return getResponseResult(response)
    }

    override suspend fun getCurrentUserTeams(): List<Team> {
        val response = client.get(Urls.TEAMS)
        return getResponseResult(response)
    }

    override suspend fun getTeam(teamId: String): TeamView {
        val response = client.get(Urls.getTeamUrl(teamId))
        return getResponseResult(response)
    }


    override suspend fun createTeam(team: Team): TeamDto {
        val response = client.post(Urls.TEAMS) {
            setBody(team)
        }
        return getResponseResult(response)
    }

    override suspend fun createTag(tag: Tag, parentRoute: ParentRoute): Tag {
        val response = client.post(Urls.getTagsUrl(parentRoute)) {
            setBody(tag)
        }
        return getResponseResult(response)
    }

    override suspend fun assignTag(
        id: String,
        parentRoute: ParentRoute,
        members: List<ActiveUser>
    ): List<ActiveUser> {
        val response = client.put(Urls.assignTag(id, parentRoute)) {
            setBody(members)
        }
        return getResponseResult(response)
    }

    override suspend fun removeMembers(
        id: String,
        parentRoute: ParentRoute,
        members: List<String>
    ): Boolean {
        val response = client.delete(Urls.removeMembers(id, parentRoute)) {
            setBody(members)
        }
        return getResponseResult(response)
    }

    override suspend fun updateTeam(team: Team): TeamDto {
        val response = client.put(Urls.TEAMS) {
            setBody(team)
        }
        return getResponseResult(response)
    }

    override suspend fun searchMembers(query: String): List<User> {
        val response = client.get(Urls.SEARCH) {
            parameter("q", query)
        }
        return getResponseResult(response)
    }

    override suspend fun deleteTeam(teamId: String): Boolean {

        val response = client.delete(Urls.getTaskUrl(teamId))
        return getResponseResult(response)

    }

    private suspend inline fun <reified T> getResponseResult(response: HttpResponse): T =
        if (response.status == HttpStatusCode.OK)
            response.body()
        else
            error(response.body<String>())

    override suspend fun registerUser(user: SignUpUserBody): Token {

        var file: File? = null
        if (user.photoPath != null)
            file = File(user.photoPath)
        val response = client.submitFormWithBinaryData(
            Urls.SIGN_UP,
            formData = formData {
                append(
                    "username",
                    user.username
                )
                append(
                    "email",
                    user.email
                )
                append(
                    "password",
                    user.password
                )
                if (file != null)
                    append(
                        "photo",
                        file.readBytes(),
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, "image/png")
                            append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                        }
                    )
            }
        )
        return getResponseResult(response)
    }
}

