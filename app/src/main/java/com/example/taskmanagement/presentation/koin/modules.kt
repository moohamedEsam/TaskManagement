package com.example.taskmanagement.presentation.koin

import android.content.Context
import android.os.Build
import android.util.Log
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.taskmanagement.MainActivityViewModel
import com.example.taskmanagement.data.data_source.IRemoteDataSource
import com.example.taskmanagement.data.data_source_impl.RemoteDataSource
import com.example.taskmanagement.data.repository.MainRepositoryImpl
import com.example.taskmanagement.domain.dataModels.utils.Token
import com.example.taskmanagement.domain.repository.IMainRepository
import com.example.taskmanagement.domain.useCases.CreateTeamUseCase
import com.example.taskmanagement.domain.useCases.LoginUserUseCase
import com.example.taskmanagement.domain.useCases.SignUpUseCase
import com.example.taskmanagement.domain.useCases.UpdateTeamUseCase
import com.example.taskmanagement.domain.utils.Urls
import com.example.taskmanagement.domain.validatorsImpl.ProfileValidator
import com.example.taskmanagement.domain.vallidators.Validator
import com.example.taskmanagement.presentation.screens.forms.project.ProjectFormViewModel
import com.example.taskmanagement.presentation.screens.forms.tags.TagViewModel
import com.example.taskmanagement.presentation.screens.forms.task.TaskFormViewModel
import com.example.taskmanagement.presentation.screens.forms.team.TeamFormViewModel
import com.example.taskmanagement.presentation.screens.home.HomeViewModel
import com.example.taskmanagement.presentation.screens.login.LoginViewModel
import com.example.taskmanagement.presentation.screens.profile.ProfileViewModel
import com.example.taskmanagement.presentation.screens.project.ProjectViewModel
import com.example.taskmanagement.presentation.screens.projects.ProjectsViewModel
import com.example.taskmanagement.presentation.screens.signUp.SignUpViewModel
import com.example.taskmanagement.presentation.screens.task.TaskViewModel
import com.example.taskmanagement.presentation.screens.team.TeamViewModel
import com.example.taskmanagement.presentation.screens.teams.TeamsViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
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

val useCaseModules = module {
    factory { LoginUserUseCase(get()) }
    factory { SignUpUseCase(get()) }
    factory { CreateTeamUseCase(get()) }
    factory { UpdateTeamUseCase(get()) }
}

fun provideRepository(remoteDataSource: IRemoteDataSource): IMainRepository =
    MainRepositoryImpl(remoteDataSource)

fun provideRemoteSource(client: HttpClient): IRemoteDataSource = RemoteDataSource(client)

val viewModelModule = module {
    viewModel { MainActivityViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { SignUpViewModel(get(), get()) }
    viewModel { params -> TaskViewModel(get(), params[0]) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { ProjectsViewModel(get()) }
    viewModel { TeamsViewModel(get()) }
    viewModel { params -> ProjectViewModel(get(), params[0]) }
    viewModel { params -> TagViewModel(get(), params[0]) }
    viewModel { params -> TeamViewModel(get(), params[0]) }
    viewModel { params -> TaskFormViewModel(get(), params[0]) }
    viewModel { params -> ProjectFormViewModel(get(), params[0], params[1]) }
    viewModel { params -> TeamFormViewModel(get(), get(), get(), params[0]) }

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
                BearerTokens(token.token, token.token)
            }
            refreshTokens {
                val accessToken = oldTokens?.accessToken ?: ""
                val refreshToken = client.post(Urls.REFRESH_TOKEN) {
                    setBody(accessToken)
                    markAsRefreshTokenRequest()
                }.body<Token>()
                if (refreshToken.token != accessToken)
                    saveToken(androidContext(), refreshToken)
                BearerTokens(refreshToken.token, refreshToken.token)
            }
            sendWithoutRequest {
                it.url.host in listOf(Urls.REFRESH_TOKEN, Urls.SIGN_IN, Urls.SIGN_UP)
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

fun saveToken(context: Context, token: Token) {
    Log.i("modules", "saveToken: ${token.token}")
    context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        .edit()
        .putString("token", token.token)
        .apply()

}

fun loadToken(context: Context): Token {
    val preferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    val token = preferences.getString("token", null)
    Log.i("MainRemoteDataSource", "getToken: $token")
    return Token(token ?: "")
}
