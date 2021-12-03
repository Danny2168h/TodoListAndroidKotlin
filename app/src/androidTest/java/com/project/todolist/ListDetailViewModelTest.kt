package com.project.todolist

import android.content.Context
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Before
import org.junit.Rule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.project.todolist.data.TodoItem
import com.project.todolist.data.TodoList
import com.project.todolist.data.database.TodoListDao
import com.project.todolist.data.database.TodoListDatabase
import com.project.todolist.data.database.TodoListRepository
import com.project.todolist.navigation.SetUpNavGraph
import com.project.todolist.screens.todolist.ListDetailedScreen
import com.project.todolist.screens.todolist.ListDetailedScreenViewModel
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListDetailViewModelTest {

    private lateinit var viewModel : ListDetailedScreenViewModel
    private lateinit var navController : NavHostController
    private lateinit var db: TodoListDatabase
    private lateinit var todoRepo : TodoListRepository
    private val todoList = TodoList("TodoList")

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    var coroutineTestRule = CoroutineTestRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        Graph.provide(context)
        db = Room.inMemoryDatabaseBuilder(
            context, TodoListDatabase::class.java).build()
        todoRepo = Graph.todoRepo
    }

    @ExperimentalComposeUiApi
    @Before
    fun beforeEach() {
        runBlockingTest {
            Graph.todoRepo.addTodoList(todoList)
        }
        composeTestRule.setContent {
            navController = rememberNavController()
            SetUpNavGraph(navController)
        }
        viewModel = ListDetailedScreenViewModel(todoList.id, navController)
    }

    @Test
    fun verifyOnTapSaveButtonSavesItemToDB() {
        lateinit var list : TodoList
        val itemTitle = "A todo item that needs completion"

        runBlockingTest {
            list = todoRepo.getListWithID(todoList.id)
        }
        assertTrue(list.todoItems.isEmpty())

        viewModel.onTapSave("A todo item that needs completion")

        runBlockingTest {
            list = todoRepo.getListWithID(todoList.id)
        }

       
    }

}