package com.example.taskmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.taskmanagement.domain.data_models.utils.UserStatus
import com.example.taskmanagement.presentation.navigation.Screens
import com.example.taskmanagement.presentation.screens.shared_layout.MainLayout
import com.example.taskmanagement.ui.theme.TaskManagementTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel by inject<MainActivityViewModel>()
        viewModel.isUserStillLoggedIn(this)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.userStatus.value != UserStatus.Loading
            }
        }
        super.onCreate(savedInstanceState)
        setContent {
            TaskManagementTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val userStatus by viewModel.userStatus
                    MainLayout(startDestination = if (userStatus is UserStatus.LoggedOut) Screens.SignIn else Screens.Home)
                }
            }
        }
    }

}