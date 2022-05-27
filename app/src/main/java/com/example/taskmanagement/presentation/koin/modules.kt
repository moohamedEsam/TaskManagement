package com.example.taskmanagement.presentation.koin

import android.os.Build
import android.util.Log
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.taskManagementWithMongoDB.presentation.utils.SplashViewModel
import com.example.taskmanagement.data.data_source.RemoteDataSource
import com.example.taskmanagement.data.data_source_impl.MainRemoteDataSource
import com.example.taskmanagement.data.repository.MainRepositoryImpl
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.validatorsImpl.MainLoginValidator
import com.example.taskmanagement.domain.vallidators.LoginValidator
import com.example.taskmanagement.presentation.screens.home.HomeViewModel
import com.example.taskmanagement.presentation.screens.login.LoginViewModel
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.Scope
import org.koin.dsl.module


val utils = module {
    single { provideCoilImageLoader() }
    single { provideHttpClient() }
    single { provideLoginValidator() }
}

val repository = module {
    single { provideRemoteSource(get()) }
    single { provideRepository(get()) }
}

fun provideRepository(remoteDataSource: RemoteDataSource): MainRepository =
    MainRepositoryImpl(remoteDataSource)

fun provideRemoteSource(client: HttpClient): RemoteDataSource = MainRemoteDataSource(client)

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }

}


fun provideHttpClient() = HttpClient(CIO) {
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Log.i("ktor", "log: $message")
            }
        }
    }
    install(ContentNegotiation) {
        json(Json {
            isLenient = true
            allowSpecialFloatingPointValues = true
            ignoreUnknownKeys = true
        })
    }

}

fun provideLoginValidator(): LoginValidator = MainLoginValidator()

private fun Scope.provideCoilImageLoader() = ImageLoader
    .Builder(androidContext())
    .components {
        if (Build.VERSION.SDK_INT >= 28)
            add(ImageDecoderDecoder.Factory())
        else
            add(GifDecoder.Factory())
    }
    .crossfade(true)
    .build()