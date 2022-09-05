package com.example.taskmanagement.presentation.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.presentation.navigation.Screens
import com.example.taskmanagement.domain.dataModels.utils.UserStatus
import com.example.taskmanagement.presentation.customComponents.PasswordTextField
import com.example.taskmanagement.presentation.customComponents.TextFieldSetup
import com.example.taskmanagement.presentation.customComponents.handleSnackBarEvent
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.inject

@Composable
fun BoxScope.LoginScreen(
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val viewModel: LoginViewModel by inject()
    val channel = viewModel.receiveChannel
    val userStatus by viewModel.userStatus.collectAsState()
    LaunchedEffect(key1 = Unit) {
        channel.collectLatest {
            handleSnackBarEvent(it, snackbarHostState)
        }
    }
    LaunchedEffect(key1 = userStatus) {
        if (userStatus is UserStatus.Authorized)
            navHostController.navigate(Screens.Home.route) {
                popUpTo(Screens.SignIn.route) { inclusive = true }
            }

    }

    LoginScreenContent(navHostController, viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.LoginScreenContent(navHostController: NavHostController, viewModel: LoginViewModel) {
    val userCredentials by viewModel.userCredentials.collectAsState()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            .align(Alignment.BottomStart)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextFieldSetup(
                value = userCredentials.email,
                label = "Email Or Username",
                validationResult = viewModel.usernameValidation,
                leadingIcon = null,
            ) {
                viewModel.setEmail(it)
            }
            Spacer(modifier = Modifier.height(8.dp))
            PasswordTextField(
                value = userCredentials.password,
                validationResult = viewModel.passwordValidation
            ) {
                viewModel.setPassword(it)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.submit()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Login", color = MaterialTheme.colorScheme.onPrimary)
            }
            Spacer(modifier = Modifier.height(32.dp))
            val signUpText = buildAnnotatedString {
                append("Don't have an account? ")
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append("Sign up")
                }
            }
            Text(
                text = signUpText,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable {
                        navHostController.navigate(Screens.SignUp.route)
                    }
            )
        }
    }

}