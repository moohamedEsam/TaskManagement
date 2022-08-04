package com.example.taskmanagement.presentation.screens.teams

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.team.Team
import com.example.taskmanagement.domain.repository.IMainRepository
import kotlinx.coroutines.launch

class TeamsViewModel(repository: IMainRepository) : ViewModel() {
    val teams = mutableStateOf(emptyList<Team>())
    val filteredTeams = mutableStateOf(emptyList<Team>())
    val searchQuery = mutableStateOf("")

    init {
        viewModelScope.launch {
            val result = repository.getUserTeams()
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