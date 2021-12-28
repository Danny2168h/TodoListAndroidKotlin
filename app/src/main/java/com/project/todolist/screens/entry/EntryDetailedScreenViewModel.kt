package com.project.todolist.screens.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.project.todolist.Graph
import com.project.todolist.MainActivity
import com.project.todolist.model.TodoItem
import com.project.todolist.model.TodoList
import com.project.todolist.model.database.TodoListRepository
import com.project.todolist.screens.entry.workers.DeleteItemWorker
import com.project.todolist.screens.todolist.threadWorkers.MoveToCompletedWorker
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import java.util.concurrent.TimeUnit

class EntryDetailedScreenViewModel(
    private val navController: NavController,
    private val listID: Long,
    private val itemID: String,
    private val todoRepo: TodoListRepository = Graph.todoRepo,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private val todoLists = todoRepo.readAllData
    private lateinit var getItemJob: Job
    private val todoItem = MutableStateFlow(TodoItem(title = ""))

    init {
        getTodoItem()
    }

    private fun getTodoItem() {
        getItemJob = viewModelScope.launch(dispatcher) {
            todoLists.collect {
                todoLists ->
                val todoList: TodoList? = todoLists.find { it.id == listID }
                val foundItem: TodoItem? = todoList!!.todoItems.find { it.uniqueID == itemID }
                if (foundItem != null) {
                    todoItem.value = foundItem!!
                }
            }
        }
    }

    fun clickSave(title: String, description: String) = viewModelScope.launch(dispatcher) {
        todoRepo.updateTodoItem(listID = listID, updatedItem = todoItem.value.copy(title = title, description = description))
    }


    fun clickReturn() {
        navController.popBackStack()
    }

    fun clickDelete() {
        val workManager = WorkManager.getInstance(MainActivity.applicationContext())
        workManager.cancelAllWorkByTag(todoItem.value.notificationID)
        val data = Data.Builder().putLong("LIST_ID", listID).putString("ITEM_ID", itemID)
        val worker = OneTimeWorkRequestBuilder<DeleteItemWorker>()
        worker.setInputData(data.build())
        workManager.enqueue(worker.build())
        navController.popBackStack()
    }
}