package com.project.todolist.mainscreen_test

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.project.todolist.MainActivity
import com.project.todolist.R
import com.project.todolist.model.TodoList
import com.project.todolist.screens.main.TodoListPairRow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodoListPairRowTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val title = "Test Item String"
    private val title1 = "Test Item String1"
    private var todoListPairBothNonNull = Pair(TodoList(title = title), TodoList(title = title1))
    private var todoListPairSecondNull = Pair(TodoList(title = title), null)

    @Test
    fun verifyBothTodoListsAreDisplayed() {
        composeTestRule.setContent {
            TodoListPairRow(
                onClickEntry = { arg1, arg2 -> twoParam(0, "") /*tested elsewhere*/ },
                onClickDelete = {/*tested elsewhere*/ },
                pairTodoList = todoListPairBothNonNull
            )
        }
        composeTestRule.onAllNodesWithTag(composeTestRule.activity.getString(R.string.individual_list))
            .assertCountEquals(2)
        composeTestRule.onNodeWithText(title)
            .assertIsDisplayed()
            .assertTextContains(title)
        composeTestRule.onNodeWithText(title1)
            .assertIsDisplayed()
            .assertTextContains(title1)
    }

    @Test
    fun verifyOneTodoListIsDisplayed() {
        composeTestRule.setContent {
            TodoListPairRow(
                onClickEntry = { arg1, arg2 -> twoParam(0, "") /*tested elsewhere*/ },
                onClickDelete = {/*tested elsewhere*/ },
                pairTodoList = todoListPairSecondNull
            )
        }
        composeTestRule.onAllNodesWithTag(composeTestRule.activity.getString(R.string.individual_list))
            .assertCountEquals(1)
        composeTestRule.onNodeWithText(title)
            .assertIsDisplayed()
            .assertTextContains(title)
    }

    private fun twoParam(num: Long, word: String) {
        //Do nothing
    }
}