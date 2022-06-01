package com.example.taskmanagement.data.data_source_impl

import android.content.Context
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
    override suspend fun loginUser(credentials: Credentials, context: Context): UserStatus {
        return try {
            val response = client.post(Urls.SIGN_IN) {
                setBody(credentials)
                contentType(ContentType.Application.Json)
            }
            if (response.status == HttpStatusCode.OK)
                UserStatus.Authorized.also {
                    saveToken(context, response.body())
                }
            else
                UserStatus.Forbidden(response.body())
        } catch (exception: Exception) {
            Log.i("MainRemoteDataSource", "loginUser: ${exception.message}")
            UserStatus.Forbidden(exception.message)
        }
    }

    override fun isUserStillLoggedIn(context: Context): Boolean {
        return loadToken(context).token.run {
            isEmpty() || isBlank()
        }.not()
    }

    override suspend fun getUserTasks(): Resource<List<Task>> {
        return try {
            val response = client.get(Urls.TASKS)
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserTask(taskId: String): Resource<TaskDetails> {
        return try {
            val response = client.get(Urls.getTaskUrl(taskId))
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun createTask(task: Task): Resource<Task> {
        return try {
            val response = client.post(Urls.TASKS) {
                setBody(task)
                contentType(ContentType.Application.Json)
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun updateTask(task: Task): Resource<Task> {
        return try {
            val response = client.put(Urls.getTaskUrl(task.id)) {
                setBody(task)
                contentType(ContentType.Application.Json)
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun deleteTask(taskId: String): Resource<ConfirmationResponse> {
        return try {
            val response = client.delete(Urls.getTaskUrl(taskId))
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserTasksByStatus(
        status: TaskStatus
    ): Resource<List<Task>> {
        return try {
            val response = client.get(Urls.getTasksByStatusUrl(status))
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserTasksByPriority(
        priority: TaskPriority
    ): Resource<List<Task>> {
        return try {
            val response = client.get(Urls.getTasksByPriorityUrl(priority))
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun createTaskComment(
        taskId: String,
        comment: Comment
    ): Resource<Task> {
        return try {
            val response = client.post(Urls.getTaskCommentsUrl(taskId)) {
                setBody(comment)
                contentType(ContentType.Application.Json)
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun deleteTaskComment(
        taskId: String,
        commentId: String
    ): Resource<Task> {
        return try {
            val response = client.delete(Urls.getTaskCommentUrl(taskId, commentId))
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun updateTaskComment(
        taskId: String,
        comment: Comment
    ): Resource<Task> {
        return try {
            val response = client.put(Urls.getTaskCommentUrl(taskId, comment.id)) {
                setBody(comment)
                contentType(ContentType.Application.Json)
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserProjects(): Resource<List<Project>> {
        return try {
            val response = client.get(Urls.PROJECTS)
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserProject(projectId: String): Resource<Project> {
        return try {
            val response = client.get(Urls.getProjectUrl(projectId))
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun createProject(project: Project): Resource<Project> {
        return try {
            val response = client.post(Urls.PROJECTS) {
                setBody(project)
                contentType(ContentType.Application.Json)
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun updateProject(project: Project): Resource<Project> {
        return try {
            val response = client.put(Urls.getProjectUrl(project.id)) {
                setBody(project)
                contentType(ContentType.Application.Json)
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun deleteProject(
        projectId: String
    ): Resource<ConfirmationResponse> {
        return try {
            val response = client.delete(Urls.getProjectUrl(projectId))
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserTeams(): Resource<List<Team>> {
        return try {
            val response = client.get(Urls.TEAMS)
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserTeam(teamId: String): Resource<Team> {
        return try {
            val response = client.get(Urls.getTeamUrl(teamId))
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun createTeam(team: Team): Resource<Team> {
        return try {
            val response = client.post(Urls.TEAMS) {
                setBody(team)
                contentType(ContentType.Application.Json)
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun updateTeam(team: Team): Resource<Team> {
        return try {
            val response = client.put(Urls.getTeamUrl(team.id)) {
                setBody(team)
                contentType(ContentType.Application.Json)
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun deleteTeam(teamId: String): Resource<Team> {
        return try {
            val response = client.delete(Urls.getTaskUrl(teamId))
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

    override suspend fun registerUser(userProfile: UserProfile, context: Context): UserStatus {
        return try {
            var file: File? = null
            if (userProfile.photoPath != null)
                file = File(userProfile.photoPath)
            val response = client.submitFormWithBinaryData(
                Urls.SIGN_UP,
                formData = formData {
                    append(
                        "username",
                        userProfile.username
                    )
                    append(
                        "email",
                        userProfile.email
                    )
                    append(
                        "password",
                        userProfile.password
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
                UserStatus.Authorized.also {
                    saveToken(context, response.body())
                }
            else
                UserStatus.Forbidden(response.body())
        } catch (exception: Exception) {
            UserStatus.Forbidden(exception.message)
        }
    }

    private fun saveToken(context: Context, token: Token) {
        Log.i("MainRemoteDataSource", "saveToken: $token")
        context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
            .edit()
            .putString("token", token.token)
            .putLong("expiresIn", token.expiresIn)
            .apply()

    }

    private fun loadToken(context: Context): Token {
        val preferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val token = preferences.getString("token", null)
        val expiresIn = preferences.getLong("expiresIn", 0)
        Log.i("MainRemoteDataSource", "getToken: $token")
        return Token(token ?: "", expiresIn)
    }

}