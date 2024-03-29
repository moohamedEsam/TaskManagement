package com.example.taskmanagement.presentation.screens.project

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.project.ProjectView
import com.example.taskmanagement.domain.dataModels.task.Task
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.ParentRoute
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.useCases.projects.GetProjectUseCase
import com.example.taskmanagement.domain.useCases.tag.AssignTagUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ProjectViewModel(
    private val getProjectUseCase: GetProjectUseCase,
    private val assignTagUseCase: AssignTagUseCase,
    private val projectId: String
) : ViewModel() {
    val project = mutableStateOf<Resource<ProjectView>>(Resource.Initialized())
    private val snackBarChannel = Channel<SnackBarEvent>()
    val receiveChannel = snackBarChannel.receiveAsFlow()
    val taggedMembers = mutableStateListOf<ActiveUserDto>()
    val updateMade = mutableStateOf(false)

    init {
        getProject()
    }

    private fun getProject() {
        viewModelScope.launch {
            project.value = getProjectUseCase(projectId)
            project.value.onSuccess {
                taggedMembers.addAll(it.members)
            }
            project.value.onError {
                val event = SnackBarEvent(it ?: "") { getProject() }
                snackBarChannel.send(event)
            }
        }
    }

    fun toggleMemberToTaggedMembers(user: User, tag: Tag) {
        updateMade.value = true
        val index = taggedMembers.indexOfFirst { user.id == it.user.id }
        if (taggedMembers[index].tag?.id != tag.id)
            taggedMembers[index] = ActiveUserDto(user, tag)
        else
            taggedMembers[index] = ActiveUserDto(user, null)
    }

    fun getGroupedTasks(): Map<String, List<Task>> {
        if (project.value.data == null)
            return emptyMap()
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

        return project.value.data!!.tasks
            .sortedBy { it.finishDate }
            .groupBy { simpleDateFormat.format(it.finishDate ?: Date()) }
    }

    fun saveTaggedMembers() {
        viewModelScope.launch {
            val result = assignTagUseCase(
                AssignTagUseCase.Params(
                    projectId,
                    ParentRoute.Projects,
                    taggedMembers.map { it.toActiveUser() })
            )
            result.onError {
                val event = SnackBarEvent(it ?: "") {
                    saveTaggedMembers()
                }
                snackBarChannel.send(event)
            }
            result.onSuccess {
                val event = SnackBarEvent("changes has been saved", null) {}
                snackBarChannel.send(event)
            }
        }
    }
}
