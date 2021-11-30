package com.project.todolist

import android.content.Context
import com.project.todolist.data.database.TodoListDatabase
import com.project.todolist.data.database.TodoListRepository

object Graph {
    lateinit var database: TodoListDatabase
        private set
    val todoRepo by lazy {
        TodoListRepository(database.todoListDao())
    }

    fun provide(context: Context) {
        database = TodoListDatabase.getDataBase(context)
    }
}