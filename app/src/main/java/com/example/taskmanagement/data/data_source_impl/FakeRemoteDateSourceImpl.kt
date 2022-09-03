package com.example.taskmanagement.data.data_source_impl

import com.example.taskmanagement.data.data_source.RemoteDataSource
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUser
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.project.Project
import com.example.taskmanagement.domain.dataModels.project.ProjectView
import com.example.taskmanagement.domain.dataModels.task.*
import com.example.taskmanagement.domain.dataModels.team.Invitation
import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.domain.dataModels.team.TeamDto
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.*
import java.util.*
import kotlin.random.Random

class FakeRemoteDateSourceImpl : RemoteDataSource {
    override suspend fun loginUser(credentials: Credentials): Token {
        return Token("")
    }

    override suspend fun registerUser(user: SignUpUserBody): Token {
        return Token("")
    }

    override suspend fun searchMembers(query: String): List<User> {
        return List(Random.nextInt(0, 100)) { User() }
    }

    override suspend fun acceptInvitation(invitationId: String): Boolean {
        return true
    }

    override suspend fun rejectInvitation(invitationId: String): Boolean {
        return true
    }

    override suspend fun getUserInvitations(): List<Invitation> {
        return List(Random.nextInt(0, 100)) {
            Invitation(
                "team",
                "team",
                "user",
                UUID.randomUUID().toString()
            )
        }
    }

    override suspend fun getCurrentUserTag(parentRoute: ParentRoute, id: String): Tag {
        return Tag()
    }

    override suspend fun createTag(tag: Tag, parentRoute: ParentRoute): Tag {
        return tag.copy(id = UUID.randomUUID().toString())
    }

    override suspend fun getCurrentUserTasks(): List<Task> {
        return List(Random.nextInt(0, 100)) { Task() }
    }

    override suspend fun getTask(taskId: String): TaskView {
        return TaskView()
    }

    override suspend fun createTask(task: Task): Task {
        return task.copy(id = UUID.randomUUID().toString())
    }

    override suspend fun updateTask(task: Task): Task {
        return task
    }

    override suspend fun deleteTask(taskId: String): ConfirmationResponse {
        return ConfirmationResponse(true)
    }

    override suspend fun getUserTasksByStatus(status: TaskStatus): List<Task> {
        return List(Random.nextInt(0, 100)) { Task() }
    }

    override suspend fun getUserTasksByPriority(priority: Priority): List<Task> {
        return List(Random.nextInt(0, 100)) { Task() }

    }

    override suspend fun createComments(comments: List<Comment>): List<Comment> {
        return List(Random.nextInt(0, 100)) { Comment() }
    }

    override suspend fun deleteTaskComment(commentId: String): ConfirmationResponse {
        return ConfirmationResponse(true)
    }

    override suspend fun updateTaskComment(comment: Comment): Comment {
        return comment
    }

    override suspend fun createOrUpdateTaskItems(
        taskId: String,
        taskItems: List<TaskItem>
    ): List<TaskItem> {
        return emptyList()
    }

    override suspend fun deleteTaskItems(taskId: String, taskItemId: String): List<TaskItem> {
        return emptyList()
    }

    override suspend fun getCurrentUserProjects(): List<Project> {
        return emptyList()
    }

    override suspend fun getProject(projectId: String): ProjectView {
        return ProjectView(owner = User())
    }

    override suspend fun createProject(project: Project): Project {
        return project
    }

    override suspend fun updateProject(project: Project): Project {
        return project
    }

    override suspend fun deleteProject(projectId: String): ConfirmationResponse {
        return ConfirmationResponse(true)
    }

    override suspend fun getCurrentUserTeams(): List<Team> {
        return emptyList()
    }

    override suspend fun getTeam(teamId: String): TeamView {
        return TeamView(
            owner = User(),
            members = List(Random.nextInt(0, 190)) { ActiveUserDto(User()) })
    }

    override suspend fun createTeam(team: Team): TeamDto {
        return TeamDto()
    }

    override suspend fun updateTeam(team: Team): TeamDto {
        return TeamDto()

    }

    override suspend fun deleteTeam(teamId: String): ConfirmationResponse {
        return ConfirmationResponse(true)
    }

    override suspend fun sendInvitation(teamId: String, userIds: List<String>): Boolean {
        return true
    }

    override suspend fun getCurrentUserProfile(): User {
        return User()
    }

    override suspend fun assignTag(
        id: String,
        parentRoute: ParentRoute,
        members: List<ActiveUser>
    ): List<ActiveUser> {
        return members
    }

    override suspend fun removeMembers(
        id: String,
        parentRoute: ParentRoute,
        members: List<String>
    ): Boolean {
        return true
    }
}