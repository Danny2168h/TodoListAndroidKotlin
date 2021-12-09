package com.project.todolist.mainscreen_test

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import com.project.todolist.MainActivity
import com.project.todolist.R
import com.project.todolist.data.TodoList
import com.project.todolist.screens.main.MainScreenMain
import org.junit.Rule
import org.junit.Test


class MainScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @Test
    fun MainScreenTest() {
        val todoLists = listOf(
            "For work",
            "Home tasks",
            "Extracurricular",
            "Tasks for friends",
            "Errands"
        )
        val testTodoLists = mutableListOf<TodoList>()
        todoLists.forEach { testTodoLists.add(TodoList(it)) }
        composeTestRule.setContent {
            MainScreenMain(
                todoLists = testTodoLists,
                onClickEntry = { /*tested elsewhere*/ },
                onTapSave = { /*tested elsewhere*/ },
                onClickDelete = { /*tested elsewhere*/ })
        }
        composeTestRule.onAllNodesWithTag(composeTestRule.activity.getString(R.string.individual_list))
            .assertCountEquals(todoLists.size)
        todoLists.forEach { composeTestRule.onNodeWithText(it).assertIsDisplayed() }
    }
}