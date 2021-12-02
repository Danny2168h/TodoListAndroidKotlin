package com.project.todolist.screens.todolist


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.project.todolist.Graph
import com.project.todolist.data.TodoItem
import com.project.todolist.data.TodoList
import com.project.todolist.data.database.TodoListRepository
import com.project.todolist.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ListDetailedScreenViewModel(id: Long, navController: NavController) : ViewModel() {

    private val todoListRepository: TodoListRepository = Graph.todoRepo
    private val todoLists = todoListRepository.readAllData
    private val todoItems = MutableStateFlow(emptyList<TodoItem>())
    private val count = MutableStateFlow(-1)
    private val selectedID = MutableStateFlow(id)
    private val currentList = MutableStateFlow(TodoList())
    private val navController = navController


    private val _state = MutableStateFlow(ListDetailedScreenViewState())
    val state: StateFlow<ListDetailedScreenViewState>
        get() = _state

    init {
        viewStateUpdater()
        dataBaseGetter()
    }

    private fun viewStateUpdater() {
        viewModelScope.launch {
            combine(todoItems, count) { todoItems: List<TodoItem>, count: Int ->
                ListDetailedScreenViewState(todoItems, count)
            }.collect {
                println("viewStateUpdater")
                _state.value = it
                println("viewStateUpdater1")
            }
        }
    }

    private fun dataBaseGetter() {
        viewModelScope.launch {
            todoLists.collect { todoLists ->
                val todoList: TodoList? = todoLists.find { it.id == selectedID.value }
                todoList?.let {
                    println("dataBaseGetter")
                    currentList.value = it
                    println("dataBaseGetter1")
                    todoItems.value = it.todoItems
                    println("dataBaseGetter2")
                }
            }
        }
    }

    fun onTapSave(todoItem: TodoItem) {
        viewModelScope.launch {
            todoListRepository.updateTodoList(currentList.value.copy(todoItems = currentList.value.todoItems + todoItem))
        }
    }

    fun onClickEntry(id : String) {
        navController.navigate(Screen.DetailedView.route + "/$id")
    }

    data class ListDetailedScreenViewState(
        val todoList: List<TodoItem> = emptyList(),
        val count: Int = -1
    )

    @Suppress("UNCHECKED_CAST")
    class ListDetailedScreenViewModelFactory(
        private val id: Long,
        private val navController: NavController
    ) :
        ViewModelProvider.Factory {
         override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ListDetailedScreenViewModel::class.java)) {
                return ListDetailedScreenViewModel(navController = navController, id = id) as T
            } else {
                throw IllegalArgumentException("unKnown view model class")
            }
        }
    }
}