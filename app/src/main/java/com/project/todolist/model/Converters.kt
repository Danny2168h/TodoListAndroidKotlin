package com.project.todolist.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.*

import com.google.gson.reflect.TypeToken as TypeToken1

class Converters {

    @TypeConverter
    fun ListTodoItemToString(listTodoItem: List<TodoItem>?): String? {
        return Gson().toJson(listTodoItem)
    }

    @TypeConverter
    fun StringToListTodoItem(data: String): List<TodoItem>? {
        if (data == null) {
            return Collections.emptyList()
        }

        val todoListType = object : TypeToken1<List<TodoItem>>() {}.type
        return Gson().fromJson<List<TodoItem>>(data, todoListType)
    }
}