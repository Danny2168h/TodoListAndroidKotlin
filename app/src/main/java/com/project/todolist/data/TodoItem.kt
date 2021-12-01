package com.project.todolist.data

data class TodoItem(
    val title: String,
    val description: String = "",
    var complete: Boolean = false
)