package com.example.taskmanagement.presentation.screens.task

import app.cash.turbine.test
import com.example.taskmanagement.data.data_source.RemoteDataSource
import com.example.taskmanagement.data.data_source_impl.RemoteDataSourceImpl
import com.example.taskmanagement.data.repository.MainRepositoryImpl
import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto
import com.example.taskmanagement.domain.dataModels.task.TaskItem
import com.example.taskmanagement.domain.dataModels.task.TaskScreenUIEvent
import com.example.taskmanagement.domain.dataModels.task.TaskStatus
import com.example.taskmanagement.domain.dataModels.task.TaskView
import com.example.taskmanagement.domain.dataModels.user.User
import com.example.taskmanagement.domain.repository.MainRepository
import com.example.taskmanagement.domain.useCases.shared.RemoveMembersUseCase
import com.example.taskmanagement.domain.useCases.tasks.GetTaskUseCase
import com.example.taskmanagement.domain.useCases.tasks.UpdateTaskUseCase
import com.example.taskmanagement.domain.useCases.tasks.comments.CreateCommentUseCase
import com.example.taskmanagement.domain.useCases.tasks.comments.UpdateCommentUseCase
import com.example.taskmanagement.domain.useCases.tasks.taskItems.CreateTaskItemsUseCase
import com.example.taskmanagement.domain.useCases.tasks.taskItems.DeleteTaskItemsUseCase
import com.example.taskmanagement.domain.useCases.tasks.taskItems.UpdateTaskItemsUseCase
import com.example.taskmanagement.domain.useCases.user.GetCurrentUserProfileUseCase
import com.example.taskmanagement.domain.validatorsImpl.BaseValidator
import com.google.common.truth.Truth.assertThat
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class TaskViewModelTest {
    private lateinit var viewModel: TaskViewModel
    private val taskId = "taskId"
    private var remoteDataSource: RemoteDataSource
    private var repository: MainRepository
    private lateinit var client: HttpClient

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThread = newSingleThreadContext("ui thread");
    private var taskView = TaskView(
        "title",
        assigned = MutableList(10) { ActiveUserDto(User("")) },
        taskItems = List(5) { TaskItem("") }
    )
    private var user = taskView.assigned.random().user

    init {
        configureClient()
        remoteDataSource = RemoteDataSourceImpl(client)
        repository = MainRepositoryImpl(remoteDataSource)
    }

    @Before
    fun setUp() = runTest {
        Dispatchers.setMain(mainThread)
        taskView = TaskView(
            "title",
            assigned = MutableList(10) { ActiveUserDto(User("")) },
            taskItems = List(5) { TaskItem("") }
        )
        user = taskView.assigned.random().user
        viewModel = TaskViewModel(
            getTaskUseCase = GetTaskUseCase(repository),
            getCurrentUserProfileUseCase = GetCurrentUserProfileUseCase(repository),
            updateTaskUseCase = UpdateTaskUseCase(repository),
            updateTaskItemsUseCase = UpdateTaskItemsUseCase(repository),
            removeMembersUseCase = RemoveMembersUseCase(repository),
            createCommentUseCase = CreateCommentUseCase(repository),
            updateCommentUseCase = UpdateCommentUseCase(repository),
            createTaskItemsUseCase = CreateTaskItemsUseCase(repository),
            deleteTaskItemsUseCase = DeleteTaskItemsUseCase(repository),
            validator = BaseValidator(),
            taskId = taskId
        )
        viewModel.setUserId().join()
        viewModel.getTask().join()
    }

    private fun configureClient() {
        val engine = MockEngine { request ->
            if (request.url.fullPath.contains("task"))
                respond(
                    content = Json.encodeToString(taskView),
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            else respond(
                content = Json.encodeToString(user),
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        client = HttpClient(engine) {
            install(ContentNegotiation) { json() }
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThread.close()
    }

    @Test
    fun `on task status click - show task status dialog is false - user not in assigned`() =
        runTest {
            user = taskView.owner
            viewModel.setUserId().join()
            viewModel.onTaskStatusClick(showSnackBarOnError = false).join()
            assertThat(viewModel.showTaskStatusDialog.value).isFalse()
        }

    @Test
    fun `toggle task status - status change to inProgress - current status is pending`() =
        runTest {
            viewModel.onTaskStatusClick(showSnackBarOnError = false).join()
            viewModel.showTaskStatusDialog.test {
                val item = awaitItem()
                assertThat(item).isTrue()
            }
        }

    @Test
    fun `on task status click - show dialog is false - current status is inProgress and task items not completed`() =
        runTest {
            viewModel.onTaskStatusConfirmClick().join()
            viewModel.onTaskStatusClick(showSnackBarOnError = false).join()
            viewModel.showTaskStatusDialog.test {
                val item = awaitItem()
                assertThat(item).isFalse()
            }
        }

    @Test
    fun `on task status click - status change to complete - current status is inProgress and task items are completed`() =
        runTest {
            viewModel.onTaskStatusConfirmClick().join()
            viewModel.taskItems.value.forEach {
                viewModel.addEventUI(
                    TaskScreenUIEvent.TaskItems.Edit(
                        it,
                        it.copy(isCompleted = true)
                    )
                )
            }
            viewModel.onTaskStatusConfirmClick().join()
            viewModel.task.test {
                val item = awaitItem()
                assertThat(item.data?.status).isEqualTo(TaskStatus.Completed)
            }
        }

    @Test
    fun `optimize events - edit event is deleted  add event is updated- edit event after add event with the same item`() =
        runTest {
            val taskItem = TaskItem("item")
            val addEvent = TaskScreenUIEvent.TaskItems.Add(taskItem)
            viewModel.addEventUI(addEvent).join()
            val editEvent =
                TaskScreenUIEvent.TaskItems.Edit(taskItem.copy(isCompleted = true), taskItem)
            viewModel.addEventUI(editEvent).join()
            viewModel.optimizeEvents().join()
            viewModel.uiEventState.test {
                val events = awaitItem()
                assertThat(events).doesNotContain(editEvent)
                val newAddEvent =
                    events.find { it is TaskScreenUIEvent.TaskItems.Add && it.taskItem == editEvent.taskItem }
                assertThat(newAddEvent).isNotNull()
            }
        }

    @Test
    fun `optimize events - edit events are deleted  add event is updated to last edit- edit events after add event with the same item`() =
        runTest {
            val taskItem = TaskItem("item")
            val addEvent = TaskScreenUIEvent.TaskItems.Add(taskItem)
            viewModel.addEventUI(addEvent).join()
            val editEvent =
                TaskScreenUIEvent.TaskItems.Edit(taskItem.copy(isCompleted = true), taskItem)
            val lastEditEvent = TaskScreenUIEvent.TaskItems.Edit(taskItem.copy(isCompleted = false), editEvent.taskItem)
            viewModel.addEventUI(editEvent).join()
            viewModel.addEventUI(lastEditEvent).join()
            viewModel.optimizeEvents().join()
            viewModel.uiEventState.test {
                val events = awaitItem()
                assertThat(events).doesNotContain(editEvent)
                assertThat(events).doesNotContain(lastEditEvent)
                val newAddEvent =
                    events.find { it is TaskScreenUIEvent.TaskItems.Add && it.taskItem == lastEditEvent.taskItem }
                assertThat(newAddEvent).isNotNull()
            }
        }

    @Test
    fun `optimize events - remove and add events are deleted - remove event after add event with the same item`() =
        runTest {
            val taskItem = TaskItem("item")
            val addEvent = TaskScreenUIEvent.TaskItems.Add(taskItem)
            viewModel.addEventUI(addEvent).join()
            val removeEvent =
                TaskScreenUIEvent.TaskItems.Remove(taskItem)
            viewModel.addEventUI(removeEvent).join()
            viewModel.optimizeEvents().join()
            viewModel.uiEventState.test {
                val events = awaitItem()
                assertThat(events).doesNotContain(removeEvent)
                assertThat(events).doesNotContain(addEvent)
            }
        }

    @Test
    fun `add event ui - update made is true - add event ui set the update made`()= runTest{
        val taskItem = TaskItem("")
        viewModel.addEventUI(TaskScreenUIEvent.TaskItems.Add(taskItem))
        viewModel.updateMade.test {
            val value = awaitItem()
            assertThat(value).isTrue()
        }
    }

    @Test
    fun `add event ui - update made is false - add event ui then remove it`()= runTest{
        val taskItem = TaskItem("")
        viewModel.addEventUI(TaskScreenUIEvent.TaskItems.Add(taskItem))
        viewModel.undoLastEvent()
        viewModel.updateMade.test {
            val value = awaitItem()
            assertThat(value).isFalse()
        }
    }
}