package com.project.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "todolist_table")
data class TodoList(
    val title: String,
    val todoItems: List<TodoItem>,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)

