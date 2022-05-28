package com.example.taskmanagement.presentation.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanagement.presentation.navigation.Screens
import com.example.taskmanagement.domain.data_models.utils.UserStatus
import com.example.taskmanagement.presentation.custom_components.PasswordTextField
import com.example.taskmanagement.presentation.custom_components.TextFieldSetup
import org.koin.androidx.compose.inject

@Composable
fun BoxScope.LoginScreen(
    navHostController: NavHostController
) {
    val viewModel: LoginViewModel by inject()
    val userStatus by viewModel.userStatus
    val snackbarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    LaunchedEffect(key1 = userStatus) {
        when (userStatus) {
            is UserStatus.Authorized -> {
                navHostController.navigate(Screens.Home.route)
            }
            is UserStatus.Forbidden -> {
                snackbarHostState.showSnackbar(message = userStatus.message ?: "")
            }
            else -> Unit
        }
    }

    LoginScreenContent(navHostController, viewModel)
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .align(Alignment.BottomStart)
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.LoginScreenContent(navHostController: NavHostController, viewModel: LoginViewModel) {
    val userCredentials by viewModel.userCredentials
    val usernameValidation by viewModel.usernameValidation
    val passwordValidation by viewModel.passwordValidation
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
                label = "Email",
                validationResult = usernameValidation,
                leadingIcon = null,
            ) {
                viewModel.setEmail(it)
            }
            Spacer(modifier = Modifier.height(8.dp))
            PasswordTextField(
                value = userCredentials.password,
                validationResult = passwordValidation
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
            Text(
                text = StringBuilder().apply {
                    append("Don't have an account? ")
                    with(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("Sign up")
                    }
                }.toString(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 8.dp)
                    .clickable {
                        navHostController.navigate(Screens.SignUp.route)
                    }
            )
        }
    }

}