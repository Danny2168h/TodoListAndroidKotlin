package com.project.todolist.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.project.todolist.Graph
import com.project.todolist.MainActivity
import com.project.todolist.model.TodoList
import com.project.todolist.model.database.TodoListRepository
import com.project.todolist.navigation.Screen
import com.project.todolist.screens.main.workers.DeleteImageWorker
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val navController: NavController,
    private val todoListRepository: TodoListRepository = Graph.todoRepo,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private val todoLists = todoListRepository.readAllData
    private val count = MutableStateFlow(0)


    private val _state = MutableStateFlow(MainScreenViewState())
    val state: StateFlow<MainScreenViewState>
        get() = _state

    init {
        createView()
    }

    private fun createView() {
        viewModelScope.launch {
            combine(todoLists, count) { todoList: List<TodoList>, count: Int ->
                MainScreenViewState(todoList, count)
            }.collect {
                _state.value = it
            }
        }
    }

    fun onTapEntry(id: Long, todoTitle: String) {
        navController.navigate(Screen.ListDetailedView.route + "/$id" + "/$todoTitle")
    }

    fun onTapSave(title: String) = viewModelScope.launch {
        todoListRepository.addTodoList(TodoList(title.trim()))
    }

    fun onTapDelete(id: Long) = viewModelScope.launch(dispatcher) {
        val workManager = WorkManager.getInstance(MainActivity.applicationContext())
        val data = Data.Builder().putLong("LIST_ID", id)
        val worker = OneTimeWorkRequestBuilder<DeleteImageWorker>()
        worker.setInputData(data.build())
        workManager.enqueue(worker.build())
        //todoListRepository.deleteTodoList(id)
    }

    data class MainScreenViewState(
        val todoLists: List<TodoList> = emptyList(),
        val count: Int = 0
    )
}