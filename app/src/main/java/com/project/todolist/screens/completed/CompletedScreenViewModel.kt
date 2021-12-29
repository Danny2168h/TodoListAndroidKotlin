package com.project.todolist.screens.completed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.project.todolist.Graph
import com.project.todolist.model.TodoItem
import com.project.todolist.model.TodoList
import com.project.todolist.model.database.TodoListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CompletedScreenViewModel(private val navController: NavController) : ViewModel() {

    private val todoListRepository: TodoListRepository = Graph.todoRepo
    private val todoLists = todoListRepository.readAllData
    private val completedTodoItems = MutableStateFlow(emptyList<TodoItem>())
    private val count = MutableStateFlow(0)


    private val _state = MutableStateFlow(CompletedScreenViewModelState())
    val state: StateFlow<CompletedScreenViewModelState>
        get() = _state


    init {
        databaseGetter()
        flowCombiner()
    }

    private fun databaseGetter() {
        viewModelScope.launch() {
            todoLists.collect { todoLists ->
                completedTodoItems.value = getAllCompletedItems(todoLists)
                count.value = completedTodoItems.value.size
            }
        }

    }

    private fun flowCombiner() {
        viewModelScope.launch {
            combine(completedTodoItems, count) { completedItems: List<TodoItem>, count: Int ->
                CompletedScreenViewModelState(completedItems, count)
            }.collect {
                _state.value = it
            }
        }
    }

    private fun getAllCompletedItems(todoLists: List<TodoList>): List<TodoItem> {
        var completedItems: List<TodoItem> = emptyList()

        for (todoList in todoLists) {
            completedItems = completedItems + todoList.completedItems
        }
        return completedItems
    }

    fun tapBack() {
        navController.popBackStack()
    }

    fun tapCompletedItem() {
        //doNothing
    }

    data class CompletedScreenViewModelState(
        val completedTodoItems: List<TodoItem> = emptyList(),
        val count: Int = 0
    )
}