package com.project.todolist.model.database

import com.project.todolist.MainActivity
import com.project.todolist.model.TodoItem
import com.project.todolist.model.TodoList

class TodoListRepository(private val todoListDao: TodoListDao) {

    val readAllData = todoListDao.readAllData()

    suspend fun addTodoList(todoList: TodoList) {
        todoListDao.addTodoList(todoList)
    }

    suspend fun deleteTodoList(id: Long) {
        todoListDao.deleteTodoList(id)
    }

    suspend fun updateTodoList(todoList: TodoList) {
        todoListDao.updateTodoList(todoList)
    }

    fun getListWithID(id: Long): TodoList {
        return todoListDao.getListWithID(id)
    }

    suspend fun deleteTodoItemFromActive(listID: Long, itemID: String?) {
        if (itemID != null) {
            val todoList = getListWithID(listID)
            updateTodoList(todoList = todoList.copy(todoItems = todoList.todoItems.filter { todoItem -> todoItem.uniqueID != itemID }))
        }
    }

    suspend fun moveTodoItemToCompleted(listID: Long, itemID: String?) {
        val todoList = getListWithID(listID)
        val todoItem: TodoItem? = todoList.todoItems.find { it.uniqueID == itemID }
        updateTodoList(
            todoList = todoList.copy(
                todoItems = todoList.todoItems.filter { todoItem -> todoItem.uniqueID != itemID },
                completedItems = todoList.completedItems + todoItem!!
            )
        )
    }

    suspend fun updateTodoItem(listID: Long, updatedItem: TodoItem) {
        val todoList = getListWithID(listID)
        updateTodoList(
            todoList = todoList.copy(
                todoItems = updateTodoItem(
                    todoList.todoItems,
                    updatedItem
                )
            )
        )
    }

    private fun updateTodoItem(todoList: List<TodoItem>, updatedItem: TodoItem): List<TodoItem> {
        for (todoItem in todoList) {
            if (todoItem.uniqueID == updatedItem.uniqueID) {
                todoItem.description = updatedItem.description
                todoItem.markedForCompletion = updatedItem.markedForCompletion
                todoItem.title = updatedItem.title
                todoItem.imagePath = updatedItem.imagePath
                todoItem.dueDate = updatedItem.dueDate
            }
        }
        return todoList
    }

}