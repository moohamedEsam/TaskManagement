package com.example.taskmanagement.presentation.screens.teams

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.utils.SnackBarEvent
import com.example.taskmanagement.domain.repository.MainRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TeamsViewModel(private val repository: MainRepository) : ViewModel() {
    val teams = mutableStateOf(emptyList<Team>())
    val filteredTeams = mutableStateOf(emptyList<Team>())
    val searchQuery = mutableStateOf("")
    private val snackBarChannel = Channel<SnackBarEvent>()
    val receiveChannel = snackBarChannel.receiveAsFlow()

    init {
        getTeams()
    }

    private fun getTeams() {
        viewModelScope.launch {
            val result = repository.getUserTeams()
            if (result is Resource.Error) {
                val event = SnackBarEvent(result.message ?: "", "Retry") {
                    getTeams()
                }
                snackBarChannel.send(event)
            }
            result.onSuccess {
                teams.value = it
            }
        }
    }

    fun setSearchQuery(value: String) {
        searchQuery.value = value
        filteredTeams.value =
            teams.value.filter { it.name.contains(value) || it.description?.contains(value) == true }
    }
}