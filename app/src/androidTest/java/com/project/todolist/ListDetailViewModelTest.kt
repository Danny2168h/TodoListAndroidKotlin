package com.project.todolist

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.project.todolist.data.TodoItem
import com.project.todolist.data.TodoList
import com.project.todolist.data.database.TodoListDatabase
import com.project.todolist.data.database.TodoListRepository
import com.project.todolist.screens.todolist.ListDetailedScreenViewModel
import com.project.todolist.screens.todolist.ListDetailedScreenViewModel.ListDetailedScreenViewState
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


//This class is still work in progress
@RunWith(AndroidJUnit4::class)
class ListDetailViewModelTest {

    private val coroutineDispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: ListDetailedScreenViewModel
    private val todoList = TodoList("TodoList", listOf(TodoItem("things to do")))
    private lateinit var db: TodoListDatabase
    private lateinit var todoListRepo: TodoListRepository

    @get:Rule
    var coroutineTestRule = CoroutineTestRule(coroutineDispatcher)

    @get:Rule
    var instantExecutor = InstantTaskExecutorRule()


    @ExperimentalComposeUiApi
    @Before
    fun BeforeEach() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, TodoListDatabase::class.java
        ).build()
        val todoListDao = db.todoListDao()
        todoListRepo = TodoListRepository(todoListDao)
        runBlockingTest { todoListRepo.addTodoList(todoList) }
        viewModel = ListDetailedScreenViewModel(
            id = todoList.id,
            todoListRepository = todoListRepo,
            dispatcher = coroutineDispatcher
        )
    }

    @After
    fun clearDB() {
        db.close()
    }

    @Test
    fun verifyOnTapSaveButtonSaveUpdatesView() = coroutineDispatcher.runBlockingTest {
        val itemTitle = "item Todo"
        assertTrue(viewModel.state.value.todoList.isEmpty())
        todoListRepo.updateTodoList(
            todoList.copy(
                todoItems = todoList.todoItems + TodoItem(itemTitle)
            )
        )
        viewModel.state.test {
            assertEquals(
                ListDetailedScreenViewState(
                    listOf(
                        TodoItem("things to do"),
                        TodoItem("item todo")
                    )
                ), awaitItem()
            )
            cancelAndConsumeRemainingEvents()
        }
    }
}
