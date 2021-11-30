package com.project.todolist.data.database

import androidx.room.*
import com.project.todolist.data.TodoList
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoListDao {

    @Query("SELECT * FROM todolist_table ORDER BY id ASC")
    fun readAllData(): Flow<List<TodoList>>

    // suspend fun deleteTodoList(todolist: TodoList)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTodoList(todolist: TodoList)

    @Update
    suspend fun updateTodoList(todolist: TodoList)
}