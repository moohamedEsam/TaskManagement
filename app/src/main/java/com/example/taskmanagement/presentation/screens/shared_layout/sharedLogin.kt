package com.example.taskmanagement.presentation.screens.shared_layout

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.taskmanagement.R
import com.example.taskmanagement.presentation.custom_components.ErrorSnackBar
import org.koin.androidx.compose.get

@Composable
fun SharedLogin(
    ui: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
    ) {
        SubcomposeAsyncImage(
            model = R.drawable.logo,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            imageLoader = get(),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .align(Alignment.TopStart),
        )
        ui()
    }
}


@Composable
fun DividerRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(modifier = Modifier.fillMaxWidth(0.45f))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "OR")
        Spacer(modifier = Modifier.width(4.dp))
        Divider(modifier = Modifier.fillMaxWidth())
    }
}
