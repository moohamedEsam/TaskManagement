package com.example.taskmanagement.presentation.screens.forms.team

import app.cash.turbine.test
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.team.TeamView
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.tag.GetCurrentUserTag
import com.example.taskmanagement.domain.useCases.teams.CreateTeamUseCase
import com.example.taskmanagement.domain.useCases.teams.GetTeamUseCase
import com.example.taskmanagement.domain.useCases.teams.UpdateTeamUseCase
import com.example.taskmanagement.domain.useCases.user.SearchMembersUseCase
import com.example.taskmanagement.domain.validatorsImpl.BaseValidator
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class TeamFormViewModelTest {

    @Mock
    private lateinit var repository: MainRepository
    private lateinit var viewModel: TeamFormViewModel
    private val teamView = TeamView("temp", members = List(10) { ActiveUserDto(User("")) })

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() = runTest {
        Dispatchers.setMain(mainThreadSurrogate)
        viewModel = TeamFormViewModel(
            searchMembersUseCase = SearchMembersUseCase(repository),
            getCurrentUserTag = GetCurrentUserTag(repository),
            getTeamUseCase = GetTeamUseCase(repository),
            updateTeamUseCase = UpdateTeamUseCase(repository),
            createTeamUseCase = CreateTeamUseCase(repository),
            validator = BaseValidator(),
            teamId = "      "
        )

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `toggle member exist in member  then doesn't exist`() = runTest {
        val user = User("owner")
        viewModel.toggleMember(user).join()
        viewModel.members.test {
            val items = awaitItem()
            assertThat(items).contains(user)
        }
        viewModel.toggleMember(user).join()
        viewModel.members.test {
            val items = awaitItem()
            assertThat(items).doesNotContain(user)
        }
    }

    @Test
    fun `search with blank query members suggestions is empty`() = runTest {
        viewModel.searchMembers("   ").join()
        viewModel.membersSuggestions.test {
            val items = awaitItem()
            assertThat(items).isEmpty()
        }
    }

    @Test
    fun `set owner owner added to members new owner removed from members`() = runTest {
        val user = teamView.members.random().user
        val oldOwner = viewModel.teamView.value.owner
        viewModel.setOwner(user).join()
        viewModel.members.test {
            val items = awaitItem()
            assertThat(items).contains(oldOwner)
            assertThat(items).doesNotContain(user)
        }
    }
}