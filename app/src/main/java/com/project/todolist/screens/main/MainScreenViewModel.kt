package com.project.todolist.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.project.todolist.Graph
import com.project.todolist.data.TodoList
import com.project.todolist.data.database.TodoListRepository
import com.project.todolist.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainScreenViewModel(private val navController: NavController) : ViewModel() {

    private val todoListRepository: TodoListRepository = Graph.todoRepo
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
                println("Main screen view model get data")
            }
        }
    }

    fun onTapEntry(id: Long, todoTitle: String) {
        navController.navigate(Screen.ListDetailedView.route + "/$id" + "/$todoTitle")
    }

    fun onTapSave(title: String) {
        viewModelScope.launch {
            todoListRepository.addTodoList(TodoList(title))
        }
    }

    fun onTapDelete(id: Long) {
        viewModelScope.launch {
            todoListRepository.deleteTodoList(id)
        }
    }

    data class MainScreenViewState(
        val todoLists: List<TodoList> = emptyList(),
        val count: Int = 0
    )
}