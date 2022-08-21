package com.example.taskmanagement.presentation.customComponents

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.fillMaxHeight(measure: (height: Int) -> Int): Modifier {
    return height(measure(LocalConfiguration.current.screenHeightDp).dp)
}

@Composable
fun Modifier.fillMaxWidth(measure: (width: Int) -> Int): Modifier {
    return width(measure(LocalConfiguration.current.screenWidthDp).dp)
}