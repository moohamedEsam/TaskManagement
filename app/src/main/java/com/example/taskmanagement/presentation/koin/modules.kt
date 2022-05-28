package com.example.taskmanagement.presentation.koin

import android.content.Context
import android.os.Build
import android.util.Log
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.taskmanagement.MainActivityViewModel
import com.example.taskmanagement.data.data_source.RemoteDataSource
import com.example.taskmanagement.data.data_source_impl.MainRemoteDataSource
import com.example.taskmanagement.data.repository.MainRepositoryImpl
import com.example.taskmanagement.domain.data_models.Token
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.utils.Urls
import com.example.taskmanagement.domain.validatorsImpl.ProfileValidator
import com.example.taskmanagement.domain.vallidators.Validator
import com.example.taskmanagement.presentation.screens.home.HomeViewModel
import com.example.taskmanagement.presentation.screens.login.LoginViewModel
import com.example.taskmanagement.presentation.screens.signUp.SignUpViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
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
    viewModel { MainActivityViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { SignUpViewModel(get(), get()) }

}


fun Scope.provideHttpClient() = HttpClient(CIO) {
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

    install(Auth) {
        bearer {
            loadTokens {
                val token = loadToken(androidContext())
                BearerTokens(token.token, "")
            }
            refreshTokens {
                val token = loadToken(androidContext())
                val refreshToken = client.get(Urls.REFRESH_TOKEN) {
                    contentType(ContentType.Application.Json)
                    setBody(token.token)
                    markAsRefreshTokenRequest()
                }.body<Token>()
                saveToken(androidContext(), token)
                BearerTokens(refreshToken.token, "")
            }
            sendWithoutRequest {
                it.url.host == Urls.SIGN_IN || it.url.host == Urls.SIGN_UP || it.url.host == Urls.REFRESH_TOKEN
            }
        }
    }

}

fun provideLoginValidator(): Validator = ProfileValidator()

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
private fun saveToken(context: Context, token: Token) {
    Log.i("modules", "saveToken: ${token.token}")
    context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        .edit()
        .putString("token", token.token)
        .putLong("expiresIn", token.expiresIn)
        .apply()

}

private fun loadToken(context: Context): Token {
    val preferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    val token = preferences.getString("token", null)
    val expiresIn = preferences.getLong("expiresIn", 0)
    Log.i("MainRemoteDataSource", "getToken: $token")
    return Token(token ?: "", expiresIn)
}
