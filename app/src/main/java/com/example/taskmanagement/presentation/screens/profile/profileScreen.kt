package com.example.taskmanagement.presentation.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.presentation.customComponents.UserIcon
import com.example.taskmanagement.presentation.navigation.Screens
import org.koin.androidx.compose.inject
import org.koin.core.parameter.parametersOf

@Composable
fun ProfileScreen(
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState,
    userId: String
) {
    val viewModel: ProfileViewModel by inject { parametersOf(userId) }
    val user by viewModel.user.collectAsState()
    user.onSuccess {
        ProfileScreenContent(navHostController, it, viewModel)
    }
}

@Composable
fun ProfileScreenContent(
    navHostController: NavHostController,
    user: User,
    viewModel: ProfileViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        ProfileScreenHeader(user, navHostController, viewModel)
        ProfileScreenBody(
            navHostController,
            user,
            viewModel = viewModel,
            modifier = Modifier.weight(1f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenBody(
    navHostController: NavHostController,
    user: User,
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        ProfileInfoCard(user.email, "Email", Icons.Outlined.Email)
        if (user.phone != null)
            ProfileInfoCard(user.phone, "Phone", Icons.Outlined.Phone)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ProfileInfoCard(value: String, label: String, imageVector: ImageVector) {
    Card {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(text = label)
                Text(text = value, color = MaterialTheme.colorScheme.onSecondaryContainer)
            }
        }
    }
}

@Composable
fun ProfileScreenHeader(
    user: User,
    navHostController: NavHostController,
    viewModel: ProfileViewModel
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Icon(
            imageVector = Icons.Default.Logout,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.End)
                .clickable {
                    viewModel.signOut()
                    navHostController.navigate(Screens.SignIn.route) {
                        popUpTo(
                            navHostController.backQueue[1].destination.route ?: Screens.SignIn.route
                        ) {
                            inclusive = true
                        }
                    }
                }
        )

        UserIcon(
            photoPath = user.photoPath,
            modifier = Modifier.size(100.dp)
        )

        Text(
            text = user.username,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 8.dp)
        )

        if (viewModel.isCurrentUser)
            Button(
                onClick = { },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )
            ) {
                Text(text = "Edit Profile")
            }
    }

}
