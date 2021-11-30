package com.project.todolist.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.project.todolist.data.Converters
import com.project.todolist.data.TodoList

@Database(entities = [TodoList::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TodoListDatabase : RoomDatabase() {

    abstract fun todoListDao(): TodoListDao

    companion object {
        @Volatile
        private var INSTANCE: TodoListDatabase? = null

        fun getDataBase(context: Context): TodoListDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    TodoListDatabase::class.java,
                    "todolist_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}