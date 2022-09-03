package com.example.taskmanagement.presentation.screens.forms.project

import app.cash.turbine.test
import com.example.taskmanagement.domain.dataModels.Tag
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.dataModels.utils.ParentRoute
import com.example.taskmanagement.domain.dataModels.utils.Resource
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.projects.CreateProjectUseCase
import com.example.taskmanagement.domain.useCases.projects.GetProjectUseCase
import com.example.taskmanagement.domain.useCases.projects.UpdateProjectUseCase
import com.example.taskmanagement.domain.useCases.tag.GetCurrentUserTag
import com.example.taskmanagement.domain.useCases.teams.GetCurrentUserTeamsUseCase
import com.example.taskmanagement.domain.useCases.teams.GetTeamUseCase
import com.example.taskmanagement.domain.validatorsImpl.BaseValidator
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class ProjectFormViewModelTest {
    @OptIn(DelicateCoroutinesApi::class)
    private val mainThread = newSingleThreadContext("ui thread")
    private lateinit var repository: MainRepository
    private lateinit var viewModel: ProjectFormViewModel
    private var user: User = User("")

    @Before
    fun setup() {
        Dispatchers.setMain(mainThread)
        repository = Mockito.mock(MainRepository::class.java)
        val getCurrentUserTag = GetCurrentUserTag(repository)
        val getTeamUseCase = GetTeamUseCase(repository)
        viewModel = ProjectFormViewModel(
            getProjectUseCase = GetProjectUseCase(repository),
            getCurrentUserTag = getCurrentUserTag,
            getCurrentUserTeamsUseCase = GetCurrentUserTeamsUseCase(repository),
            getTeamUseCase = getTeamUseCase,
            createProjectUseCase = CreateProjectUseCase(repository),
            updateProjectUseCase = UpdateProjectUseCase(repository),
            validator = BaseValidator(),
            teamId = "      ",
            projectId = "       "
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThread.close()
    }

    @Test
    fun `changeOwner oldOwnerShouldBeInMembers`() = runTest {
        val oldOwner = viewModel.projectView.value.owner
        viewModel.setProjectOwner(user)
        viewModel.projectView.test {
            val project = awaitItem()
            assertThat(project.owner.id).isEqualTo(user.id)
        }
        viewModel.members.test {
            val members = awaitItem()
            assertThat(members.contains(oldOwner.id)).isTrue()
            assertThat(members.contains(user.id)).isFalse()
        }
    }


    @Test
    fun `toggleMemberTwice shouldBeAddedThenRemoved`() = runTest {
        viewModel.toggleProjectMember(user).join()
        viewModel.members.test {
            val members = awaitItem()
            assertThat(members).contains(user.id)
        }
        viewModel.toggleProjectMember(user).join()
        viewModel.members.test {
            val members = awaitItem()
            assertThat(members).doesNotContain(user.id)
        }
    }
}