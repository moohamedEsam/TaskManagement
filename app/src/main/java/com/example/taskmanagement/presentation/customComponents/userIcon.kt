package com.example.taskmanagement.presentation.customComponents

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.taskmanagement.R
import com.example.taskmanagement.domain.dataModels.User
import com.example.taskmanagement.domain.utils.Urls
import org.koin.androidx.compose.get

@Composable
fun UserIcon(
    user: User,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(Urls.getUserImage(user.photoPath) ?: R.drawable.profile_person)
            .crossfade(true)
            .build(),
        imageLoader = get(),
        contentScale = ContentScale.Crop,
        contentDescription = null,
        loading = {
            CircularProgressIndicator()
        },
        modifier = Modifier
            .border(
                2.dp,
                color = if (user.photoPath != null) Color.Black else Color.Transparent,
                shape = CircleShape
            )
    )
}