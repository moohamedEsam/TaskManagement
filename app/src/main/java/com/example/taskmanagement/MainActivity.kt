package com.example.taskmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isUserAuthorized.value is Resource.Initialized
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
                    val isUserAuthorized by viewModel.isUserAuthorized.collectAsState()
                    val startDestination by remember {
                        derivedStateOf {
                            if (isUserAuthorized is Resource.Success && isUserAuthorized.data == true) Screens.Home
                            else Screens.SignIn
                        }
                    }
                    MainLayout(startDestination)
                }
            }
        }

    }
}