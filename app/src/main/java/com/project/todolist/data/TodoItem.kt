package com.project.todolist.data

data class TodoItem(
    var title: String,
    var description: String = "",
    var complete: Boolean = false
)