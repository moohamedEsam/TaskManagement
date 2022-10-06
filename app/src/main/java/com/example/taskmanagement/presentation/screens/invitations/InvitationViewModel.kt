package com.example.taskmanagement.presentation.screens.invitations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.domain.dataModels.team.Invitation
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.useCases.user.AcceptInvitationUseCase
import com.example.taskmanagement.domain.useCases.user.DeclineInvitationUseCase
import com.example.taskmanagement.domain.useCases.user.GetCurrentUserInvitationsUseCase
import com.example.taskmanagement.presentation.koin.viewModelModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InvitationViewModel(
    private val acceptInvitationUseCase: AcceptInvitationUseCase,
    private val declineInvitationUseCase: DeclineInvitationUseCase,
    private val getCurrentUserInvitationsUseCase: GetCurrentUserInvitationsUseCase
) : ViewModel() {
    private val _invitations = MutableStateFlow<Resource<List<Invitation>>>(Resource.Initialized())
    val invitations = _invitations.asStateFlow()

    init {
        viewModelScope.launch {
            _invitations.update { getCurrentUserInvitationsUseCase(Unit) }
        }
    }

    fun acceptInvitation(invitation: Invitation) = viewModelScope.launch {
        val result = acceptInvitationUseCase(invitation.id)
        removeInvitationFromList(result, invitation)
    }

    private fun removeInvitationFromList(
        result: Resource<Boolean>,
        invitation: Invitation
    ) {
        result.onSuccess { success ->
            if (success)
                _invitations.update {
                    it.copy(it.data?.minus(invitation))
                }
        }
    }

    fun declineInvitation(invitation: Invitation) = viewModelScope.launch {
        val result = declineInvitationUseCase(invitation.id)
        removeInvitationFromList(result, invitation)
    }

}