package com.project.todolist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.project.todolist.data.TodoItem
import com.project.todolist.screens.todolist.TodoListUI
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodoListUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun verifyTodoListsDisplaysAllTodo() {
        val items = listOf(
            "Walk the dog",
            "Take out the trash",
            "Do homework",
            "Eat breakfast",
            "Finish coding"
        )
        val testTodoList = mutableListOf<TodoItem>()
        items.forEach { testTodoList.add(TodoItem(it)) }
        composeTestRule.setContent {
            TodoListUI(
                onClickEntry = { /*Tested in TodoItemTest*/ }, entries = testTodoList
            )
        }
        items.forEach { composeTestRule.onNodeWithText(it).assertIsDisplayed() }
    }

}