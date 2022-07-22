package com.example.taskmanagement.presentation.screens.team

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.repository.IMainRepository
import kotlinx.coroutines.launch

class TeamViewModel(
    private val repository: IMainRepository,
    private val teamId: String
) : ViewModel() {
    val team = mutableStateOf<Resource<TeamView>>(Resource.Initialized())
    init {
        getTeam()
    }

    fun getTeam() = viewModelScope.launch {
        team.value = repository.getUserTeam(teamId)
    }
}