package com.example.taskmanagement.presentation.screens.forms.project

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.project.ProjectView
import com.example.taskmanagement.domain.dataModels.task.Permission
import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.ParentRoute
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.dataModels.utils.ValidationResult
import com.example.taskmanagement.domain.useCases.projects.CreateProjectUseCase
import com.example.taskmanagement.domain.useCases.projects.GetProjectUseCase
import com.example.taskmanagement.domain.useCases.projects.UpdateProjectUseCase
import com.example.taskmanagement.domain.useCases.tag.GetCurrentUserTag
import com.example.taskmanagement.domain.useCases.teams.GetCurrentUserTeamsUseCase
import com.example.taskmanagement.domain.useCases.teams.GetTeamUseCase
import com.example.taskmanagement.domain.validatorsImpl.BaseValidator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProjectFormViewModel(
    private val getProjectUseCase: GetProjectUseCase,
    private val getCurrentUserTeamsUseCase: GetCurrentUserTeamsUseCase,
    private val getCurrentUserTag: GetCurrentUserTag,
    private val getTeamUseCase: GetTeamUseCase,
    private val createProjectUseCase: CreateProjectUseCase,
    private val updateProjectUseCase: UpdateProjectUseCase,
    private val validator: BaseValidator,
    private val teamId: String,
    private val projectId: String
) : ViewModel() {
    private val _team = MutableStateFlow<Resource<TeamView>>(Resource.Initialized())
    val team = _team.asStateFlow()
    private val _teams = MutableStateFlow<Resource<List<Team>>>(Resource.Initialized())
    val teams = _teams.asStateFlow()
    val isUpdating = projectId.isNotBlank()
    private var currentUserPermission: Resource<Tag> = Resource.Initialized()
    private val _projectView = MutableStateFlow(getInitializedProjectView())
    val projectView = _projectView.asStateFlow()
    private val snackbarChannel = Channel<SnackBarEvent>()
    val receiveChannel = snackbarChannel.receiveAsFlow()
    private val _members = MutableStateFlow(emptySet<String>())
    val members = _members.asStateFlow()
    val showTeamDialog = mutableStateOf(false)
    private val _projectNameValidationResult = MutableStateFlow(ValidationResult(true))
    val projectNameValidationResult = _projectNameValidationResult.asStateFlow()
    val canSave =
        combine(_team, _projectNameValidationResult) { team, projectNameValidationResult ->
            team is Resource.Success && projectNameValidationResult.isValid
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    init {
        viewModelScope.launch {
            if (teamId.isNotBlank())
                assignTeam(teamId)

            if (isUpdating) {
                getUserTag()
                getProject()
            }
        }
    }

    private suspend fun getProject() {
        val result = getProjectUseCase(projectId)
        result.onSuccess { project ->
            _projectView.update { project }
            _members.update { project.members.map { it.user.id }.toSet() }
        }
        result.onError {
            val event = SnackBarEvent(it ?: "") {
                getProject()
            }
            snackbarChannel.send(event)
        }

    }

    fun setTeams() {
        viewModelScope.launch {
            if (_teams.value is Resource.Loading) return@launch
            _teams.update { Resource.Loading() }
            _teams.update { getCurrentUserTeamsUseCase(Unit) }
            teams.value.onError {
                val event = SnackBarEvent(it ?: "") { setTeams() }
                snackbarChannel.send(event)
            }
        }
    }

    private fun getUserTag() {
        viewModelScope.launch {
            currentUserPermission =
                getCurrentUserTag(GetCurrentUserTag.Params(ParentRoute.Projects, projectId))
            currentUserPermission.onError {
                val event = SnackBarEvent(it ?: "") { getUserTag() }
                snackbarChannel.send(event)
            }
        }
    }

    fun setTeam(id: String) {
        viewModelScope.launch {
            assignTeam(id)
        }
    }

    private suspend fun assignTeam(id: String) {
        _team.update {
            var result = getTeamUseCase(id)
            result.onSuccess { teamView ->
                result =
                    result.copy(teamView.copy(members = teamView.members + ActiveUserDto(teamView.owner)))
            }
            _members.update { emptySet() }
            _projectView.update { it.copy(team = id) }
            result
        }
        team.value.onError {
            val event = SnackBarEvent(it ?: "") { setTeam(id) }
            snackbarChannel.send(event)
        }
    }

    private fun getInitializedProjectView() = ProjectView("")

    fun toggleProjectMember(user: User) = viewModelScope.launch {
        _members.update {
            if (it.contains(user.id))
                it - user.id
            else
                it + user.id
        }
    }

    fun setProjectName(name: String) {
        _projectView.update { it.copy(name = name) }
        _projectNameValidationResult.update { validator.nameValidator.validate(name) }
    }

    fun setProjectDescription(description: String) {
        _projectView.update { it.copy(description = description) }
    }

    fun setProjectOwner(owner: User) {
        _members.update {
            it + projectView.value.owner.id - owner.id
        }
        _projectView.update { it.copy(owner = owner) }
    }

    fun saveProject(onLoading: () -> Unit, onDone: () -> Unit) {
        viewModelScope.launch {
            _projectNameValidationResult.update { validator.nameValidator.validate(projectView.value.name) }
            if (!_projectNameValidationResult.value.isValid)
                return@launch
            onLoading()
            val project =
                projectView.value.toProject()
                    .copy(members = getSelectedMembers())
            val result = if (isUpdating)
                updateProjectUseCase(project)
            else
                createProjectUseCase(project)
            onDone()
            result.onSuccess {
                val event = SnackBarEvent("project has been saved", null) {}
                snackbarChannel.send(event)
            }
            result.onError {
                val event = SnackBarEvent(it ?: "") { saveProject(onLoading, onDone) }
                snackbarChannel.send(event)
            }
        }
    }

    private fun getSelectedMembers() =
        team.value.data?.members?.filter { members.value.contains(it.user.id) }
            ?.map { it.toActiveUser() } ?: emptyList()


    fun hasPermission(requiredPermission: Permission): Boolean {
        return currentUserPermission.data?.isUserAuthorized(requiredPermission) ?: true
    }

    fun toggleTeamDialog() {
        showTeamDialog.value = !showTeamDialog.value
    }
}