package com.example.taskmanagement.presentation.screens.signUp

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.example.taskmanagement.R
import com.example.taskmanagement.domain.dataModels.utils.UserStatus
import com.example.taskmanagement.presentation.customComponents.PasswordTextField
import com.example.taskmanagement.presentation.customComponents.TextFieldSetup
import com.example.taskmanagement.presentation.customComponents.handleSnackBarEvent
import com.example.taskmanagement.presentation.navigation.Screens
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.get
import org.koin.androidx.compose.inject

@Composable
fun SignUpScreen(navHostController: NavHostController, snackbarHostState: SnackbarHostState) {
    val viewModel: SignUpViewModel by inject()
    val userStatus by viewModel.userStatus.collectAsState()
    val channel = viewModel.receiveChannel
    LaunchedEffect(key1 = Unit) {
        channel.collectLatest {
            handleSnackBarEvent(it, snackbarHostState)
        }
    }
    LaunchedEffect(key1 = userStatus) {
        if (userStatus is UserStatus.Authorized)
            navHostController.navigate(Screens.Home.route) {
                popUpTo(Screens.SignIn.route) { inclusive = true }
            }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        SignUpForm(viewModel)
    }

}

@Composable
fun SignUpForm(viewModel: SignUpViewModel) {
    val user by viewModel.user.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    var image: Bitmap? by remember {
        mutableStateOf(null)
    }
    val context = LocalContext.current

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        it.data?.data?.let { uri ->
            context.contentResolver.query(uri, null, null, null, null).use { cursor ->
                if (cursor != null) {
                    cursor.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    val path = cursor.getString(columnIndex)
                    viewModel.setPhotoPath(path)
                }
            }
            image = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        context.contentResolver,
                        uri
                    )
                )
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
        }
    }
    val storagePermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickImage.launch(intent)
        }

    }
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        SubcomposeAsyncImage(
            model = if (image == null) R.drawable.profile_person else image,
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable {
                    storagePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                },
            imageLoader = get(),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextFieldSetup(
            value = user.email,
            label = "Email",
            validationResult = viewModel.emailValidationResult,
            leadingIcon = null
        ) {
            viewModel.setEmail(it)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextFieldSetup(
            value = user.username,
            label = "Username",
            validationResult = viewModel.usernameValidationResult,
            leadingIcon = null
        ) {
            viewModel.setUsername(it)
        }

        Spacer(modifier = Modifier.height(8.dp))

        PasswordTextField(
            value = user.password,
            validationResult = viewModel.passwordValidationResult,
        ) {
            viewModel.setPassword(it)
        }
        Spacer(modifier = Modifier.height(8.dp))
        PasswordTextField(
            value = confirmPassword,
            label = "Confirm Password",
            validationResult = viewModel.confirmPasswordValidationResult,
        ) {
            viewModel.setConfirmPassword(it)
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextFieldSetup(
            value = user.phone ?: "",
            label = "Phone",
            validationResult = viewModel.phoneValidationResult,
            leadingIcon = null
        ) {
            viewModel.setPhone(it)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                viewModel.submit()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Sign Up", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
