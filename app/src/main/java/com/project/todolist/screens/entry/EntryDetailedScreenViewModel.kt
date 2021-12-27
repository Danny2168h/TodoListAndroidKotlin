package com.project.todolist.screens.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.project.todolist.Graph
import com.project.todolist.model.TodoItem
import com.project.todolist.model.TodoList
import com.project.todolist.model.database.TodoListRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EntryDetailedScreenViewModel(
    private val navController: NavController,
    private val listID: Long,
    private val itemID: String,
    private val todoRepo: TodoListRepository = Graph.todoRepo,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private val todoLists = todoRepo.readAllData
    private val todoItem = MutableStateFlow(TodoItem(title = ""))

    init {
        getTodoItem()
    }

    private fun getTodoItem() {
        viewModelScope.launch(dispatcher) {
            todoLists.collect {
                todoLists ->
                val todoList: TodoList? = todoLists.find { it.id == listID }
                val foundItem: TodoItem? = todoList!!.todoItems.find { it.uniqueID == itemID }
                todoItem.value = foundItem!!
            }
        }
    }

    fun clickSave(title: String, description: String) = viewModelScope.launch(dispatcher) {
        todoRepo.updateTodoItem(listID = listID, updatedItem = todoItem.value.copy(title = title, description = description))
    }


    fun clickReturn() {
        navController.popBackStack()
    }

    fun clickDelete() = viewModelScope.launch(dispatcher) {
        todoRepo.deleteTodoItemFromActive(listID = listID, itemID = itemID)
    }
}