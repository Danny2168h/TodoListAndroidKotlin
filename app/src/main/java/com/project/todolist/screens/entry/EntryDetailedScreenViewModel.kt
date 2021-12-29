package com.project.todolist.screens.entry

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.io.IOException

class EntryDetailedScreenViewModel(
    private val navController: NavController,
    private val listID: Long,
    private val itemID: String,
    private val todoRepo: TodoListRepository = Graph.todoRepo,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private val todoLists = todoRepo.readAllData
    private val todoItem = MutableStateFlow(TodoItem(title = ""))
    private val todoImage = MutableStateFlow<Bitmap?>(null)
    private val todoDueDate = MutableStateFlow("")

    private val _state = MutableStateFlow(EntryDetailedScreenState())
    val state: StateFlow<EntryDetailedScreenState>
        get() = _state

    init {
        getTodoItem()
        viewStateUpdater()
    }

    private fun viewStateUpdater() {
        viewModelScope.launch(dispatcher) {
            combine(todoImage, todoDueDate) { image, date ->
                EntryDetailedScreenState(image, date)
            }.collect {
                _state.value = it
            }
        }
    }

    private fun getTodoItem() {
        viewModelScope.launch {
            todoLists.collect { todoLists ->
                val todoList: TodoList? = todoLists.find { it.id == listID }
                if (todoList != null) {
                    val foundItem: TodoItem? = todoList!!.todoItems.find { it.uniqueID == itemID }
                    if (foundItem != null) {
                        todoItem.value = foundItem!!
                        todoDueDate.value = foundItem!!.dueDate
                        if (todoItem.value.imagePath != null) {
                            val bytes = MainActivity.applicationContext()
                                .openFileInput("${todoItem.value.uniqueID}.jpeg").readBytes()
                            val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                            todoImage.value = bmp
                        } else {
                            todoImage.value = null
                        }
                    }
                }
            }
        }
    }

    fun clickSave(title: String, description: String, image: Bitmap?) =
        viewModelScope.launch(dispatcher) {
            if (image != null) {
                try {
                    MainActivity.applicationContext()
                        .openFileOutput("${todoItem.value.uniqueID}.jpeg", Context.MODE_PRIVATE)
                        .use { stream ->
                            if (!image.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                                throw IOException("Image Cannot be Saved")
                            }
                        }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (image == null && todoItem.value.imagePath != null) {
                MainActivity.applicationContext().deleteFile(todoItem.value.imagePath)
            }

            todoRepo.updateTodoItem(
                listID = listID,
                updatedItem = todoItem.value.copy(
                    title = title,
                    description = if (description.isEmpty()) {
                        " "
                    } else {
                        description
                    },
                    imagePath = if (image != null) {
                        "${todoItem.value.uniqueID}.jpeg"
                    } else {
                        null
                    }
                )
            )
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

    data class EntryDetailedScreenState(
        val todoImage: Bitmap? = null,
        val todoDueDate: String = ""

    )
}