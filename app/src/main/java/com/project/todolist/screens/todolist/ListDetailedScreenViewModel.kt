package com.project.todolist.screens.todolist


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.common.util.concurrent.ListenableFuture
import com.project.todolist.Graph
import com.project.todolist.MainActivity
import com.project.todolist.model.TodoItem
import com.project.todolist.model.TodoList
import com.project.todolist.model.database.TodoListRepository
import com.project.todolist.navigation.Screen
import com.project.todolist.screens.todolist.threadWorkers.MoveToCompletedWorker
import com.project.todolist.screens.todolist.threadWorkers.NotificationWorker
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit


class ListDetailedScreenViewModel(
    private val id: Long,
    private val navController: NavHostController,
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
        dataBaseGetter()
        viewStateUpdater()
    }

    private fun viewStateUpdater() {
        viewModelScope.launch(dispatcher) {
            combine(
                todoItems,
                count,
            ) { todoItems: List<TodoItem>, count: Int ->
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
                    count.value = it.todoItems.size
                }
            }
        }
    }

    fun onTapSave(title: String, itemDueTime: String) {
        viewModelScope.launch(dispatcher) {
            val item = TodoItem(title.trim())
            todoListRepository.updateTodoList(
                currentList.value.copy(
                    todoItems = currentList.value.todoItems + item
                )
            )
            val dueDate = SimpleDateFormat("dd/MM/yyyy hh:mm").parse(itemDueTime)
            val difference = (dueDate.time / 1000) - (System.currentTimeMillis() / 1000)

            println(System.currentTimeMillis() / 1000)
            println(dueDate.time / 1000)
            println(difference)

            val workManager = WorkManager.getInstance(MainActivity.applicationContext())
            val data = Data.Builder()
            data.putLong("LIST_ID", id)
            data.putString("ITEM_ID", item.uniqueID)
            data.putString("DUE_DATE", itemDueTime)
            val worker = OneTimeWorkRequestBuilder<NotificationWorker>()
            worker.setInitialDelay(difference, TimeUnit.SECONDS)
            worker.setInputData(data.build())
            worker.addTag(item.uniqueID)
            workManager.enqueue(worker.build())

        }
    }

    fun onTapEntry(title: String, description: String, id: String) {
        navController.navigate(Screen.DetailedView.route + "/$title" + "/$description" + "/${currentList.value.id}" + "/$id")
    }

    fun onClickMainMenu() {
        navController.navigate(Screen.MainScreen.route)
    }

    fun onClickCompleted() {
        navController.navigate(Screen.CompletedScreen.route)
    }

    fun onTapEntryComplete(todoItem: TodoItem) = viewModelScope.launch(dispatcher) {
        todoListRepository.updateTodoItem(
            id,
            todoItem.copy(
                markedForCompletion = !todoItem.markedForCompletion,
                uniqueID = todoItem.uniqueID
            )
        )
        val uniqueID = todoItem.uniqueID

        val workManager = WorkManager.getInstance(MainActivity.applicationContext())
        if (isWorkScheduled(uniqueID)) {
            workManager.cancelAllWorkByTag(uniqueID)
        } else {
            workManager.cancelAllWorkByTag(uniqueID)
            val data = Data.Builder()
            data.putLong("LIST_ID", id)
            data.putString("ITEM_ID", uniqueID)
            val worker = OneTimeWorkRequestBuilder<MoveToCompletedWorker>()
            worker.setInitialDelay(60, TimeUnit.SECONDS)
            worker.setInputData(data.build())
            worker.addTag(uniqueID)
            workManager.enqueue(worker.build())
        }
    }

    fun onClickCheck(newTitle: String) = viewModelScope.launch(dispatcher) {
        todoListRepository.updateTodoList(
            currentList.value.copy(
                title = newTitle.trim()
            )
        )
    }


    data class ListDetailedScreenViewState(
        val todoList: List<TodoItem> = emptyList(),
        val count: Int = -1,
    )

    private fun isWorkScheduled(tag: String): Boolean {
        val instance = WorkManager.getInstance(MainActivity.applicationContext())
        val statuses: ListenableFuture<List<WorkInfo>> = instance.getWorkInfosByTag(tag)
        return try {
            var running = false
            val workInfoList: List<WorkInfo> = statuses.get()
            for (workInfo in workInfoList) {
                val state = workInfo.state
                running = (state == WorkInfo.State.RUNNING) or (state == WorkInfo.State.ENQUEUED)
            }
            running
        } catch (e: ExecutionException) {
            e.printStackTrace()
            false
        } catch (e: InterruptedException) {
            e.printStackTrace()
            false
        }
    }

}
