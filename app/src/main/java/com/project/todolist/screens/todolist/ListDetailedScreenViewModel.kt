package com.project.todolist.screens.todolist


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.todolist.Graph
import com.project.todolist.data.TodoItem
import com.project.todolist.data.TodoList
import com.project.todolist.data.database.TodoListRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ListDetailedScreenViewModel(
    id: Long,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val todoListRepository: TodoListRepository = Graph.todoRepo
) : ViewModel() {

    private val todoLists = todoListRepository.readAllData
    private val todoItems = MutableStateFlow(emptyList<TodoItem>())
    private val count = MutableStateFlow(-1)
    private val selectedID = MutableStateFlow(id)
    private val currentList = MutableStateFlow(TodoList())


    private val _state = MutableStateFlow(ListDetailedScreenViewState())
    val state: StateFlow<ListDetailedScreenViewState>
        get() = _state

    init {
        viewStateUpdater()
        dataBaseGetter()
    }

    private fun viewStateUpdater() {
        viewModelScope.launch(dispatcher) {
            combine(todoItems, count) { todoItems: List<TodoItem>, count: Int ->
                ListDetailedScreenViewState(todoItems, count)
            }.collect {
                _state.value = it
            }
        }
    }

    private fun dataBaseGetter() {
        viewModelScope.launch(dispatcher) {
            todoLists.collect { todoLists ->
                val todoList: TodoList? = todoLists.find { it.id == selectedID.value }
                todoList?.let {
                    currentList.value = it
                    todoItems.value = it.todoItems
                }
            }
        }
    }

    fun onTapSave(title: String) {
        viewModelScope.launch(dispatcher) {
            todoListRepository.updateTodoList(
                currentList.value.copy(
                    todoItems = currentList.value.todoItems + TodoItem(title)
                )
            )
        }
    }

    data class ListDetailedScreenViewState(
        val todoList: List<TodoItem> = emptyList(),
        val count: Int = -1
    )

}
