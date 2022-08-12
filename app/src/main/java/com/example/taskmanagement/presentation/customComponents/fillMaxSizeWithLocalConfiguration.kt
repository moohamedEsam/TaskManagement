package com.example.taskmanagement.presentation.customComponents

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.fillMaxHeight(ratio: Int) =
    Modifier.height((LocalConfiguration.current.screenHeightDp / ratio).dp)

@Composable
fun Modifier.fillMaxWidth(ratio: Int) =
    Modifier.width((LocalConfiguration.current.screenWidthDp / ratio).dp)