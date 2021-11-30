package com.project.todolist.data.database

import com.project.todolist.data.TodoList

class TodoListRepository(private val todoListDao: TodoListDao) {

    val readAllData = todoListDao.readAllData()

    suspend fun addTodoList(todoList: TodoList) {
        todoListDao.addTodoList(todoList)
    }

//    suspend fun deleteTodoList(todoList : TodoList) {
//        todoListDao.deleteTodoList(todoList)
//    }

    suspend fun updateTodoList(todoList: TodoList) {
        todoListDao.addTodoList(todoList)
    }

}