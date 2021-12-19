package com.project.todolist.model

import java.util.*

data class TodoItem(
    var title: String,
    var description: String = " ",
    var markedForCompletion: Boolean = false,
    val uniqueID: String = UUID.randomUUID().toString()
)