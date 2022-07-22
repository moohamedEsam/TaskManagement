package com.example.taskmanagement.presentation.customComponents

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.taskmanagement.R
import com.example.taskmanagement.domain.utils.Urls
import org.koin.androidx.compose.get

@Composable
fun UserIcon(
    photoPath: String?,
    size: Dp = 24.dp,
    modifier: Modifier = Modifier
) {
    val model = ImageRequest.Builder(LocalContext.current)
        .data(Urls.getUserImage(photoPath) ?: R.drawable.profile_person)
        .crossfade(true)

    if (photoPath != null)
        model.transformations(CircleCropTransformation())

    SubcomposeAsyncImage(
        model = model.build(),
        imageLoader = get(),
        contentScale = ContentScale.Crop,
        contentDescription = null,
        loading = {
            CircularProgressIndicator()
        },
        modifier = modifier
            .size(size)
            .border(
                2.dp,
                color = if (photoPath != null) Color.Black else Color.Transparent,
                shape = CircleShape
            )
    )
}