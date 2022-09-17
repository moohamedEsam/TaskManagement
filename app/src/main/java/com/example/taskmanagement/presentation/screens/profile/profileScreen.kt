package com.example.taskmanagement.presentation.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.dataModels.user.UserView
import com.example.taskmanagement.presentation.customComponents.HandleResourceChange
import com.example.taskmanagement.presentation.customComponents.UserIcon
import com.example.taskmanagement.presentation.navigation.Screens
import org.koin.androidx.compose.inject

@Composable
fun ProfileScreen(navHostController: NavHostController, snackbarHostState: SnackbarHostState) {
    val viewModel: ProfileViewModel by inject()
    val user by viewModel.user
    HandleResourceChange(
        resource = user,
        onSuccess = {},
        snackbarHostState = snackbarHostState,
        onSnackbarClick = { viewModel.getUser() }
    )
    ProfileScreenContent(navHostController, user, viewModel)
}

@Composable
fun ProfileScreenContent(
    navHostController: NavHostController,
    user: Resource<User>,
    viewModel: ProfileViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        user.onSuccess {
            ProfileScreenHeader(it, navHostController)
            ProfileScreenBody(navHostController, it, viewModel = viewModel)
        }
    }
}

@Composable
fun ProfileScreenBody(
    navHostController: NavHostController,
    user: User,
    viewModel: ProfileViewModel
) {
    val context = LocalContext.current
    Column {
        Text(text = "Projects", style = MaterialTheme.typography.headlineMedium)
        LazyRow {

        }
    }
}

@Composable
fun ProfileScreenHeader(user: User, navHostController: NavHostController) {
    val viewModel: ProfileViewModel by inject()
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
    }

}
