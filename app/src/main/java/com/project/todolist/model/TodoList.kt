package com.project.todolist.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todolist_table")
data class TodoList(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    val todoItems: List<TodoItem> = emptyList(),
    val completedItems: List<TodoItem> = emptyList()
)

