package com.example.taskmanagement.presentation.koin

import android.content.Context
import android.os.Build
import android.util.Log
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.taskmanagement.MainActivityViewModel
import com.example.taskmanagement.data.data_source.RemoteDataSource
import com.example.taskmanagement.data.data_source_impl.RemoteDataSourceImpl
import com.example.taskmanagement.data.repository.MainRepositoryImpl
import com.example.taskmanagement.domain.dataModels.utils.Token
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.projects.CreateProjectUseCase
import com.example.taskmanagement.domain.useCases.projects.GetCurrentUserProjectUseCase
import com.example.taskmanagement.domain.useCases.projects.GetProjectUseCase
import com.example.taskmanagement.domain.useCases.projects.UpdateProjectUseCase
import com.example.taskmanagement.domain.useCases.shared.GetCurrentUserStatusUseCase
import com.example.taskmanagement.domain.useCases.shared.ObserveUserStatusUseCase
import com.example.taskmanagement.domain.useCases.shared.RemoveMembersUseCase
import com.example.taskmanagement.domain.useCases.tag.AssignTagUseCase
import com.example.taskmanagement.domain.useCases.tag.CreateTagUseCase
import com.example.taskmanagement.domain.useCases.tag.GetCurrentUserTag
import com.example.taskmanagement.domain.useCases.tasks.*
import com.example.taskmanagement.domain.useCases.tasks.comments.CreateCommentUseCase
import com.example.taskmanagement.domain.useCases.tasks.comments.DeleteCommentUseCase
import com.example.taskmanagement.domain.useCases.tasks.comments.UpdateCommentUseCase
import com.example.taskmanagement.domain.useCases.tasks.taskItems.CreateTaskItemsUseCase
import com.example.taskmanagement.domain.useCases.tasks.taskItems.DeleteTaskItemsUseCase
import com.example.taskmanagement.domain.useCases.tasks.taskItems.UpdateTaskItemsUseCase
import com.example.taskmanagement.domain.useCases.teams.CreateTeamUseCase
import com.example.taskmanagement.domain.useCases.teams.GetCurrentUserTeamsUseCase
import com.example.taskmanagement.domain.useCases.teams.GetTeamUseCase
import com.example.taskmanagement.domain.useCases.teams.UpdateTeamUseCase
import com.example.taskmanagement.domain.useCases.teams.invitation.SendInvitationUseCase
import com.example.taskmanagement.domain.useCases.user.*
import com.example.taskmanagement.domain.utils.Urls
import com.example.taskmanagement.domain.validatorsImpl.BaseValidator
import com.example.taskmanagement.domain.validatorsImpl.ProfileValidator
import com.example.taskmanagement.domain.validatorsImpl.TaskFormValidator
import com.example.taskmanagement.presentation.screens.forms.project.ProjectFormViewModel
import com.example.taskmanagement.presentation.screens.forms.tags.TagViewModel
import com.example.taskmanagement.presentation.screens.forms.task.TaskFormViewModel
import com.example.taskmanagement.presentation.screens.forms.team.TeamFormViewModel
import com.example.taskmanagement.presentation.screens.home.HomeViewModel
import com.example.taskmanagement.presentation.screens.invitations.InvitationViewModel
import com.example.taskmanagement.presentation.screens.login.LoginViewModel
import com.example.taskmanagement.presentation.screens.sharedLayout.MainLayoutViewModel
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
import io.ktor.client.plugins.*
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
    single { ProfileValidator() }
    single { BaseValidator() }
    single { TaskFormValidator() }
    single { GetCurrentUserStatusUseCase(androidContext(), get()) }
    single { ObserveUserStatusUseCase(get()) }
    single { LogoutUseCase(get(), androidContext()) }
}

val repository = module {
    single { provideRemoteSource(get()) }
    single { provideRepository(get()) }
}

val teamUserCasesModule = module {
    single { CreateTeamUseCase(get()) }
    single { UpdateTeamUseCase(get()) }
    single { GetCurrentUserTeamsUseCase(get()) }
    single { GetTeamUseCase(get()) }
    single { SendInvitationUseCase(get()) }
    single { AssignTagUseCase(get()) }
    single { CreateTagUseCase(get()) }
    single { GetCurrentUserTag(get()) }
    single { RemoveMembersUseCase(get()) }

}

val projectUseCasesModule = module {
    single { GetCurrentUserProjectUseCase(get()) }
    single { CreateProjectUseCase(get()) }
    single { UpdateProjectUseCase(get()) }
    single { GetProjectUseCase(get()) }
}

val taskUseCasesModule = module {
    single { CreateTaskUseCase(get()) }
    single { UpdateTaskUseCase(get()) }
    single { UpdateTaskStatusUseCase(get()) }
    single { GetTaskUseCase(get()) }
    single { CreateCommentUseCase(get()) }
    single { UpdateCommentUseCase(get()) }
    single { DeleteCommentUseCase(get()) }
    single { UpdateTaskItemsUseCase(get()) }
    single { CreateTaskItemsUseCase(get()) }
    single { DeleteTaskItemsUseCase(get()) }
    single { GetCurrentUserTasksUseCase(get()) }
}

val userUseCasesModule = module {
    single { LoginUserUseCase(get(), androidContext()) }
    single { SignUpUseCase(get(), androidContext()) }
    single { GetCurrentUserProfileUseCase(get()) }
    single { GetUserProfileUseCase(get()) }
    single { GetCurrentUserDashboardUseCase(get()) }
    single { SearchMembersUseCase(get()) }
    single { AcceptInvitationUseCase(get()) }
    single { DeclineInvitationUseCase(get()) }
    single { GetCurrentUserInvitationsUseCase(get()) }
}

fun provideRepository(remoteDataSource: RemoteDataSource): MainRepository =
    MainRepositoryImpl(remoteDataSource)

fun provideRemoteSource(client: HttpClient): RemoteDataSource = RemoteDataSourceImpl(client)

val viewModelModule = module {
    viewModel { MainActivityViewModel(get()) }
    viewModel { MainLayoutViewModel(get(), get(), get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { SignUpViewModel(get(), get()) }
    viewModel { params ->
        TaskViewModel(
            getTaskUseCase = get(),
            getCurrentUserProfileUseCase = get(),
            deleteCommentUseCase = get(),
            updateTaskStatusUseCase = get(),
            updateCommentUseCase = get(),
            updateTaskItemsUseCase = get(),
            removeMembersUseCase = get(),
            createCommentUseCase = get(),
            createTaskItemsUseCase = get(),
            deleteTaskItemsUseCase = get(),
            validator = get(),
            taskId = params[0]
        )
    }
    viewModel { params -> ProfileViewModel(get(), get(), get(), params[0]) }
    viewModel { ProjectsViewModel(get()) }
    viewModel { TeamsViewModel(get()) }
    viewModel { params -> ProjectViewModel(get(), get(), params[0]) }
    viewModel { params -> TagViewModel(get(), params[0], params[1]) }
    viewModel { params -> TeamViewModel(get(), get(), get(), get(), get(), params[0]) }
    viewModel { params ->
        TaskFormViewModel(
            getTaskUseCase = get(),
            getCurrentUserTag = get(),
            getCurrentUserProjectUseCase = get(),
            getProjectUseCase = get(),
            createTaskUseCase = get(),
            updateTaskUseCase = get(),
            validator = get(),
            projectId = params[0],
            taskId = params[1]
        )
    }
    viewModel { params ->
        ProjectFormViewModel(
            getProjectUseCase = get(),
            getCurrentUserTag = get(),
            getCurrentUserTeamsUseCase = get(),
            getTeamUseCase = get(),
            createProjectUseCase = get(),
            updateProjectUseCase = get(),
            validator = get(),
            teamId = params[0],
            projectId = params[1],
            getCurrentUserProfileUseCase = get()
        )
    }
    viewModel { params ->
        TeamFormViewModel(
            searchMembersUseCase = get(),
            getCurrentUserTag = get(),
            getTeamUseCase = get(),
            updateTeamUseCase = get(),
            createTeamUseCase = get(),
            validator = get(),
            teamId = params[0]
        )
    }
    viewModel { InvitationViewModel(get(), get(), get()) }

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
                val token = loadToken(androidContext())
                val refreshToken = client.post(Urls.REFRESH_TOKEN) {
                    setBody(token.token)
                    markAsRefreshTokenRequest()
                }.body<Token>()
                if (refreshToken.token != token.token)
                    saveToken(androidContext(), refreshToken)
                BearerTokens(refreshToken.token, refreshToken.token)
            }
        }
    }
}


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
    context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        .edit()
        .putString("token", token.token)
        .putLong("expiresAt", token.expiresAt)
        .apply()

}

fun loadToken(context: Context): Token {
    val preferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    val token = preferences.getString("token", "") ?: ""
    val expiresAt = preferences.getLong("expiresAt", 0)
    return Token(token, expiresAt)
}
