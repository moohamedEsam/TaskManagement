package com.example.taskmanagement.presentation.screens.teams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.useCases.teams.GetCurrentUserTeamsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TeamsViewModel(private val getCurrentUserTeamsUseCase: GetCurrentUserTeamsUseCase) :
    ViewModel() {
    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams = _teams.asStateFlow()
    private val _filteredTeams = MutableStateFlow(emptyList<Team>())
    val filteredTeams = _filteredTeams.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    private val snackBarChannel = Channel<SnackBarEvent>()
    val receiveChannel = snackBarChannel.receiveAsFlow()

    init {
        getTeams()
    }

    private fun getTeams() {
        viewModelScope.launch {
            val result = getCurrentUserTeamsUseCase(Unit)
            result.onSuccess { data->
                _teams.update { data }
            }
            result.onError {
                val event = SnackBarEvent(it ?: "") { getTeams() }
                snackBarChannel.send(event)
            }
        }
    }

    fun setSearchQuery(value: String) {
        _searchQuery.update { value }
        _filteredTeams.update {
            teams.value.filter { team -> team.name.contains(value) || team.description?.contains(value) == true }
        }
    }
}