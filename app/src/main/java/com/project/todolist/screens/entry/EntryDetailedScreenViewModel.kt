package com.project.todolist.screens.entry

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.project.todolist.model.TodoItem

class EntryDetailedScreenViewModel(
    private val navController: NavController,
    private val listID: Long?,
    private val itemID: String?
) : ViewModel() {

    fun updateTodoEntry(listID: Long, entry: TodoItem) {

    }

}