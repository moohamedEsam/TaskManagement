package com.example.taskmanagement.domain.utils

import com.example.taskmanagement.domain.dataModels.task.Priority
import com.example.taskmanagement.domain.dataModels.task.TaskStatus
import com.example.taskmanagement.domain.dataModels.team.Invitation
import com.example.taskmanagement.domain.dataModels.utils.ParentRoute

object Urls {
    private const val BASE_URL = "http://192.168.1.7:8080"
    const val REFRESH_TOKEN = "$BASE_URL/auth/refresh"
    const val PROFILE = "$BASE_URL/user"
    const val INVITATION = "$BASE_URL/user/invitations"
    const val SIGN_IN = "$BASE_URL/user/signIn"
    const val SIGN_UP = "$BASE_URL/user/signUp"
    const val TASKS = "$BASE_URL/tasks"
    const val COMMENTS = "$BASE_URL/comments"
    const val TEAMS = "$BASE_URL/teams"
    const val PROJECTS = "$BASE_URL/projects"
    fun searchMembers(query: String) = "$PROFILE/search/$query"

    fun getAcceptInvitationUrl(invitationId: String) =
        "$INVITATION/$invitationId/accept"

    fun getRejectInvitationUrl(invitationId: String) =
        "$INVITATION/$invitationId/reject"

    fun getTaskItemRoute(id: String) = "${getTaskUrl(id)}/taskItems"
    fun getDeleteTaskItemRoute(id: String, taskItemId:String) = "${getTaskUrl(id)}/taskItems/$taskItemId"

    fun getTagsUrl(parentRoute: ParentRoute) = "$BASE_URL/tags/$parentRoute"

    fun getTaskUrl(id: String) = "$BASE_URL/tasks/$id"
    fun getTaskCommentsUrl(id: String) = "$BASE_URL/comments/$id"
    fun getUserTag(parentRoute: String, id: String) = "$BASE_URL/$parentRoute/$id/userTag"

    fun getTasksByStatusUrl(status: TaskStatus) = "$BASE_URL/tasks?status=$status"
    fun getTasksByPriorityUrl(priority: Priority) = "$BASE_URL/tasks?priority=$priority"

    fun getTeamUrl(id: String) = "$BASE_URL/teams/$id"
    fun assignTag(id: String, parentRoute: ParentRoute) = "$BASE_URL/$parentRoute/$id/assignTag"
    fun getProjectUrl(id: String) = "$BASE_URL/projects/$id"

    fun getUserImage(id: String?) = if (id != null) "$BASE_URL/user/Images/$id" else null
}