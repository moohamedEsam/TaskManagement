package com.example.taskmanagement.data.data_source_impl

import android.util.Log
import com.example.taskmanagement.data.data_source.IRemoteDataSource
import com.example.taskmanagement.domain.dataModels.project.Project
import com.example.taskmanagement.domain.dataModels.task.*
import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.*
import com.example.taskmanagement.domain.dataModels.project.ProjectView
import com.example.taskmanagement.domain.dataModels.task.TaskView
import com.example.taskmanagement.domain.dataModels.team.TeamDto
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.utils.Urls
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.io.File

class RemoteDataSource(private val client: HttpClient) : IRemoteDataSource {
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
            Log.i("MainRemoteDataSource", "loginUser: ${exception.message}")
            UserStatus.Forbidden(exception.message)
        }
    }

    override suspend fun getUserProfile(): Resource<User> {
        return try {
            val response = client.get(Urls.PROFILE) {
                headers.names().forEach {
                    Log.i("RemoteDataSource", "getUserProfile: $it")
                }
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }


    override suspend fun getUserTasks(): Resource<List<Task>> {
        return try {
            val response = client.get(Urls.TASKS)
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun getUserTask(taskId: String): Resource<TaskView> {
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
        priority: Priority
    ): Resource<List<Task>> {
        return try {
            val response = client.get(Urls.getTasksByPriorityUrl(priority))
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun createTaskComment(
        comment: Comment
    ): Resource<Comment> {
        return try {
            val response = client.post(Urls.COMMENTS) {
                setBody(comment)
                contentType(ContentType.Application.Json)
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun deleteTaskComment(
        commentId: String
    ): Resource<ConfirmationResponse> {
        return try {
            val response = client.delete(Urls.COMMENTS)
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun updateTaskComment(
        comment: Comment
    ): Resource<Comment> {
        return try {
            val response = client.put(Urls.COMMENTS) {
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

    override suspend fun getUserProject(projectId: String): Resource<ProjectView> {
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

    override suspend fun getUserTeam(teamId: String): Resource<TeamView> {
        return try {
            val response = client.get(Urls.getTeamUrl(teamId))
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }


    override suspend fun createTeam(team: Team): Resource<TeamDto> {
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

    override suspend fun updateTeam(team: Team): Resource<TeamDto> {
        return try {
            val response = client.put(Urls.TEAMS) {
                setBody(team)
                contentType(ContentType.Application.Json)
            }
            getResponseResource(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun searchMembers(query: String): Resource<List<User>> {
        return try {
            val response = client.get(Urls.searchMembers(query))
            getResponseResource<List<User>>(response)
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

    override suspend fun deleteTeam(teamId: String): Resource<ConfirmationResponse> {
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

    override suspend fun registerUser(user: SignUpUserBody): Resource<Token> {
        return try {
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
            if (response.status == HttpStatusCode.OK)
                Resource.Success(response.body())
            else
                Resource.Error(response.body())
        } catch (exception: Exception) {
            Resource.Error(exception.message)
        }
    }

}