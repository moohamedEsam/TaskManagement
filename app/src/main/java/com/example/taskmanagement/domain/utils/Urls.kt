package com.example.taskmanagement.domain.utils

import com.example.taskmanagement.domain.dataModels.TaskPriority
import com.example.taskmanagement.domain.dataModels.TaskStatus

object Urls {
    private const val BASE_URL = "http://192.168.1.6:8080"
    const val REFRESH_TOKEN = "$BASE_URL/auth/refresh"
    const val PROFILE = "$BASE_URL/user"
    const val SIGN_IN = "$BASE_URL/user/signIn"
    const val SIGN_UP = "$BASE_URL/user/signUp"
    const val TASKS = "$BASE_URL/tasks"
    const val TEAMS = "$BASE_URL/teams"
    const val PROJECTS = "$BASE_URL/projects"

    fun getTaskUrl(id: String) = "$BASE_URL/tasks/$id"
    fun getTaskCommentsUrl(id: String) = "$BASE_URL/tasks/$id/comments"
    fun getTaskCommentUrl(taskId: String, commentId: String) =
        "$BASE_URL/tasks/$taskId/comments/$commentId"

    fun getTasksByStatusUrl(status: TaskStatus) = "$BASE_URL/tasks?status=$status"
    fun getTasksByPriorityUrl(priority: TaskPriority) = "$BASE_URL/tasks?priority=$priority"

    fun getTeamUrl(id: String) = "$BASE_URL/teams/$id"
    fun getProjectUrl(id: String) = "$BASE_URL/projects/$id"

    fun getUserImage(id: String?) = if (id != null) "$BASE_URL/user/Images/$id" else null
}