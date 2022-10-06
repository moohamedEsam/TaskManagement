package com.example.taskmanagement.presentation.screens.invitations

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.taskmanagement.domain.dataModels.team.Invitation
import org.koin.androidx.compose.inject

@Composable
fun InvitationsScreen() {
    val viewModel: InvitationViewModel by inject()
    val invitations by viewModel.invitations.collectAsState()
    invitations.onSuccess {
        InvitationsScreenContent(invitations = it, viewModel = viewModel)
    }
}

@Composable
private fun InvitationsScreenContent(
    invitations: List<Invitation>,
    viewModel: InvitationViewModel
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(invitations) { invitation ->
            InvitationItem(invitation = invitation, viewModel = viewModel)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitationItem(invitation: Invitation, viewModel: InvitationViewModel) {
    Card {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
            Text(
                text = buildAnnotatedString {
                    append("you were invited to join")
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append(" ${invitation.teamName} ")
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.align(Alignment.End),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.declineInvitation(invitation) },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(text = "Decline")
                }

                Button(
                    onClick = { viewModel.acceptInvitation(invitation) },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(text = "Accept")
                }
            }
        }
    }
}
