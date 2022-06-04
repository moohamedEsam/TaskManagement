package com.example.taskmanagement.presentation.screens.sharedLayout

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage
import com.example.taskmanagement.R
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
