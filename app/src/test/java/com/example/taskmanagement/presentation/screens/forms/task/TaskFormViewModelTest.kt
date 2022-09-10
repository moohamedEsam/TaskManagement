package com.example.taskmanagement.presentation.screens.forms.task

import app.cash.turbine.test
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.project.ProjectView
import com.example.taskmanagement.domain.dataModels.task.TaskItem
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.projects.GetCurrentUserProjectUseCase
import com.example.taskmanagement.domain.useCases.projects.GetProjectUseCase
import com.example.taskmanagement.domain.useCases.tag.GetCurrentUserTag
import com.example.taskmanagement.domain.useCases.tasks.CreateTaskUseCase
import com.example.taskmanagement.domain.useCases.tasks.GetTaskUseCase
import com.example.taskmanagement.domain.useCases.tasks.UpdateTaskUseCase
import com.example.taskmanagement.domain.validatorsImpl.TaskFormValidator
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class TaskFormViewModelTest {
    @OptIn(DelicateCoroutinesApi::class)
    private val mainThread = newSingleThreadContext("ui thread")
    private var repository: MainRepository
    private var viewModel: TaskFormViewModel
    private var projectView: ProjectView = ProjectView("project", members = List(10) { ActiveUserDto(User("")) })

    init {
        Dispatchers.setMain(mainThread)
        repository = Mockito.mock(MainRepository::class.java)
        val getTaskUseCase = GetTaskUseCase(repository)
        val getProjectUseCase = GetProjectUseCase(repository)
        viewModel = TaskFormViewModel(
            getProjectUseCase = getProjectUseCase,
            getTaskUseCase = getTaskUseCase,
            getCurrentUserProjectUseCase = GetCurrentUserProjectUseCase(repository),
            createTaskUseCase = CreateTaskUseCase(repository),
            updateTaskUseCase = UpdateTaskUseCase(repository),
            validator = TaskFormValidator(),
            getCurrentUserTag = GetCurrentUserTag(repository),
            projectId = "  ",
            taskId = "      "
        )

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThread.close()
    }

    @Test
    fun `createTask setTaskTitleWithBlank nameValidationResultIsFalse`() = runTest {
        viewModel.setTaskTitle("    ")
        viewModel.taskTitleValidationResult.test {
            val item = awaitItem()
            assertThat(item.isValid).isFalse()
        }
    }

    @Test
    fun `addTaskItem blankTitle lastItemValidationResultIsFalse`() = runTest {
        val taskItem = TaskItem("    ")
        viewModel.validateTaskItemTitle(taskItem.title).join()
        viewModel.addTaskItem(taskItem).join()
        viewModel.taskItemValidationResult.test {
            val item = awaitItem()
            assertThat(item.isValid).isFalse()
        }
        viewModel.taskItems.test {
            val items = awaitItem()
            assertThat(items).doesNotContain(taskItem)
        }
    }

    @Test
    fun `addEstimatedTime negative estimatedTimeValidationResultIsFalse`() = runTest {
        viewModel.setTaskEstimatedTime("-15")
        viewModel.taskEstimatedTimeValidationResult.test {
            val item = awaitItem()
            assertThat(item.isValid).isFalse()
        }
    }

    @Test
    fun `addDate past finishDateValidationResultIsFalse`() = runTest {
        viewModel.setTaskFinishDate(Date(System.currentTimeMillis() - 15))
        viewModel.taskFinishDateValidationResult.test {
            val item = awaitItem()
            assertThat(item.isValid).isFalse()
        }
    }

    @Test
    fun `toggleTaskAssigned toggleTwice MemberExistThenNotExist`() = runTest {
        val user = projectView.members.random().user
        viewModel.toggleTaskAssigned(user.id).join()
        viewModel.assigned.test {
            val item = awaitItem()
            assertThat(item).contains(user.id)
        }
        viewModel.toggleTaskAssigned(user.id).join()
        viewModel.assigned.test {
            val item = awaitItem()
            assertThat(item).doesNotContain(user.id)
        }
    }

    @Test
    fun `setMilestoneTitle blank taskMilestoneTitleValidationResultIsFalse`() = runTest {
        viewModel.validateTaskItemTitle("       ")
        viewModel.setTaskMilestoneTitle("   ")
        viewModel.taskMilestoneTitleValidationResult.test {
            val item = awaitItem()
            assertThat(item.isValid).isFalse()
        }
    }

}