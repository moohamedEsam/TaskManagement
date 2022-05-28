package com.example.taskmanagement.data.data_source_impl

import android.util.Log
import com.example.taskmanagement.data.data_source.RemoteDataSource
import com.example.taskmanagement.domain.data_models.*
import com.example.taskmanagement.domain.data_models.utils.*
import com.example.taskmanagement.domain.utils.Urls
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.io.File

class MainRemoteDataSource(private val client: HttpClient) : RemoteDataSource {
    override suspend fun loginUser(credentials: Credentials): UserStatus {
        return try {
            val response = client.post(Urls.SIGN_IN) {
                setBody(credentials)
                contentType(ContentType.Application.Json)
            }
            if (response.status == HttpStatusCode.OK)
                UserStatus.Authorized(response.body())
            else
                UserStatus.Forbidden(response.body())
        } catch (exception: Exception) {
            UserStatus.Forbidden(exception.message)
        }
    }

    override suspend fun getUserTasks(token: Token): Resource<List<Task>> {
        return try {
            val response = client.get(Urls.TASKS) {
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserTask(token: Token, taskId: String): Resource<Task> {
        return try {
            val response = client.get(Urls.getTaskUrl(taskId)) {
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun createTask(token: Token, task: Task): Resource<Task> {
        return try {
            val response = client.post(Urls.TASKS) {
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
                setBody(task)
                contentType(ContentType.Application.Json)
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun updateTask(token: Token, task: Task): Resource<Task> {
        return try {
            val response = client.put(Urls.getTaskUrl(task.id)) {
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
                setBody(task)
                contentType(ContentType.Application.Json)
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun deleteTask(token: Token, taskId: String): Resource<ConfirmationResponse> {
        return try {
            val response = client.delete(Urls.getTaskUrl(taskId)) {
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserTasksByStatus(
        token: Token,
        status: TaskStatus
    ): Resource<List<Task>> {
        return try {
            val response = client.get(Urls.getTasksByStatusUrl(status)) {
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserTasksByPriority(
        token: Token,
        priority: TaskPriority
    ): Resource<List<Task>> {
        return try {
            val response = client.get(Urls.getTasksByPriorityUrl(priority)) {
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun createTaskComment(
        token: Token,
        taskId: String,
        comment: Comment
    ): Resource<Task> {
        return try {
            val response = client.post(Urls.getTaskCommentsUrl(taskId)) {
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
                setBody(comment)
                contentType(ContentType.Application.Json)
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun deleteTaskComment(
        token: Token,
        taskId: String,
        commentId: String
    ): Resource<Task> {
        return try {
            val response = client.delete(Urls.getTaskCommentUrl(taskId, commentId)) {
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun updateTaskComment(
        token: Token,
        taskId: String,
        comment: Comment
    ): Resource<Task> {
        return try {
            val response = client.put(Urls.getTaskCommentUrl(taskId, comment.id)) {
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
                setBody(comment)
                contentType(ContentType.Application.Json)
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserProjects(token: Token): Resource<List<Project>> {
        return try {
            val response = client.get(Urls.PROJECTS) {
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserProject(token: Token, projectId: String): Resource<Project> {
        return try {
            val response = client.get(Urls.getProjectUrl(projectId)) {
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun createProject(token: Token, project: Project): Resource<Project> {
        return try {
            val response = client.post(Urls.PROJECTS) {
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
                setBody(project)
                contentType(ContentType.Application.Json)
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun updateProject(token: Token, project: Project): Resource<Project> {
        return try {
            val response = client.put(Urls.getProjectUrl(project.id)) {
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
                setBody(project)
                contentType(ContentType.Application.Json)
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun deleteProject(
        token: Token,
        projectId: String
    ): Resource<ConfirmationResponse> {
        return try {
            val response = client.delete(Urls.getProjectUrl(projectId)) {
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserTeams(token: Token): Resource<List<Team>> {
        return try {
            val response = client.get(Urls.TEAMS) {
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserTeam(token: Token, teamId: String): Resource<Team> {
        return try {
            val response = client.get(Urls.getTeamUrl(teamId)) {
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun createTeam(token: Token, team: Team): Resource<Team> {
        return try {
            val response = client.post(Urls.TEAMS) {
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
                setBody(team)
                contentType(ContentType.Application.Json)
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun updateTeam(token: Token, team: Team): Resource<Team> {
        return try {
            val response = client.put(Urls.getTeamUrl(team.id)) {
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
                setBody(team)
                contentType(ContentType.Application.Json)
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun deleteTeam(token: Token, teamId: String): Resource<Team> {
        return try {
            val response = client.delete(Urls.getTaskUrl(teamId)) {
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    private suspend inline fun <reified T> getResponseResource(response: HttpResponse): Resource<T> =
        if (response.status == HttpStatusCode.OK)
            Resource.Success(response.body())
        else
            Resource.Error(response.body())

    override suspend fun registerUser(registerUser: RegisterUser): UserStatus {
        return try {
            var file: File? = null
            if (registerUser.photoPath != null)
                file = File(registerUser.photoPath)
            val response = client.submitFormWithBinaryData(
                Urls.SIGN_UP,
                formData = formData {
                    append(
                        "username",
                        registerUser.username
                    )
                    append(
                        "email",
                        registerUser.email
                    )
                    append(
                        "password",
                        registerUser.password
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
            if (response.status == HttpStatusCode.OK)
                return UserStatus.Authorized(response.body())
            else
                UserStatus.Forbidden(response.body())
        } catch (exception: Exception) {
            UserStatus.Forbidden(exception.message)
        }
    }
}