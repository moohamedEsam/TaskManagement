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
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.presentation.navigation.Screens
import com.example.taskmanagement.presentation.screens.sharedLayout.MainLayout
import com.example.taskmanagement.ui.theme.TaskManagementTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel by inject<MainActivityViewModel>()
        viewModel.getCurrentUserProfile()
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.user.value is Resource.Loading || viewModel.user.value is Resource.Initialized
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
                    val user by viewModel.user
                    if (user is Resource.Initialized)
                        return@Surface
                    MainLayout(
                        startDestination = if (user is Resource.Success) Screens.Home else Screens.SignIn,
                        user = user.data
                    )
                }
            }
        }
    }

}