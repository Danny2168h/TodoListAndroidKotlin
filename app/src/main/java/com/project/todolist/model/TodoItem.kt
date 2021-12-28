package com.project.todolist.model

import android.graphics.Bitmap
import java.util.*

data class TodoItem(
    var title: String,
    var description: String = " ",
    var markedForCompletion: Boolean = false,
    var imageBm: Bitmap? = null,
    var imagePath: String? = null,
    var dueDate: String = "",
    val uniqueID: String = UUID.randomUUID().toString(),
    val notificationID: String = UUID.randomUUID().toString(),
)