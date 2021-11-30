package com.project.todolist.testData

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.project.todolist.data.TodoItem
import java.util.*

class ScreenUiData {
    var listOfTodo: SnapshotStateList<TodoItem> = mutableStateListOf()
    val dateCurrent = Calendar.getInstance().getTime().toString().substring(0, 10)

    init {
        listOfTodo.add(TodoItem("Walk the dog"))
    }

    fun addItem(item: TodoItem) {
        listOfTodo.add(item)
    }

    fun editItem(item: TodoItem) {
        item.title = "New Description"
    }
}