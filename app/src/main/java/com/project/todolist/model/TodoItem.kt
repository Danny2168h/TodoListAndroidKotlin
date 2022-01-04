package com.project.todolist.model

import java.util.*

data class TodoItem(
    val title: String,
    val description: String = " ",
    val markedForCompletion: Boolean = false,
    val imagePath: String? = null,
    val dueDate: String = "",
    val uniqueID: String = UUID.randomUUID().toString(),
    val notificationID: String = UUID.randomUUID().toString(),
)