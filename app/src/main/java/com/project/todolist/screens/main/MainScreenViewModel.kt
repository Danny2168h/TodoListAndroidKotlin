package com.project.todolist.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.todolist.Graph
import com.project.todolist.data.TodoItem
import com.project.todolist.data.TodoList
import com.project.todolist.data.database.TodoListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainScreenViewModel() : ViewModel() {

    private val todoListRepository: TodoListRepository = Graph.todoRepo
    private val todoLists = todoListRepository.readAllData
    private val count = MutableStateFlow(0)


    private val _state = MutableStateFlow(MainScreenViewState())
    val state: StateFlow<MainScreenViewState>
        get() = _state

    init {
        createView()
        //updateView()
    }

//    private fun updateView() {
//        viewModelScope.launch {
//            todoListRepository.readAllData.collect { todoLists ->
//                todoLists.find {
//                    it.id == selectId.value
//                }.also {
//                    selectId.value = it?.id ?: -1
//                    if (selectId.value != -1L) {
//                        todoText.value = it?.todo ?: ""
//                        todoTime.value = it?.time ?: ""
//                    }
//                }
//            }
//        }
//    }

    private fun createView() {
        viewModelScope.launch() {
            combine(todoLists, count) { todoList: List<TodoList>, count: Int ->
                MainScreenViewState(todoList, count)
            }.collect {
                _state.value = it
            }
        }
    }

    fun onButtonTap() {
        viewModelScope.launch {
            todoListRepository.addTodoList(
                TodoList(
                    "First List", listOf(TodoItem("Walk dog"), TodoItem("Add Persistence"))
                )
            )
        }
    }

    data class MainScreenViewState(
        val todoLists: List<TodoList> = emptyList(),
        val count: Int = 0
    )
}