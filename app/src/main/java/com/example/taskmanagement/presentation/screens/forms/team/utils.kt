package com.example.taskmanagement.presentation.screens.forms.team

import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.example.taskmanagement.domain.dataModels.Permission
import com.example.taskmanagement.domain.dataModels.Role
import com.example.taskmanagement.domain.dataModels.utils.TeamUser

fun SnapshotStateMap<String, List<Permission>>.toTeamUser(key: String) =
    TeamUser(key, Role(get(key) ?: emptyList()))