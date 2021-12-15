package com.project.todolist.list_detailed_screen_test

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.project.todolist.MainActivity
import com.project.todolist.R
import com.project.todolist.model.TodoItem
import com.project.todolist.screens.todolist.TodoItemUI
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodoItemTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var title: String
    private var buttonClicked = false

    lateinit var rightArrow: SemanticsNodeInteraction
    lateinit var titleItem: SemanticsNodeInteraction

    @Before
    fun setup() {
        title = getRandomString(20)
        buttonClicked = false
        composeTestRule.setContent {
            TodoItemUI(
                onClickEntry = { buttonClicked = true },
                entry = TodoItem(title),
                onTapEntryComplete = { /* Tested elsewhere*/ }
            )
        }
        rightArrow = composeTestRule.onNodeWithTag(
            composeTestRule.activity.getString(R.string.arrow_right),
            useUnmergedTree = true
        )
        titleItem = composeTestRule.onNodeWithText(title)
    }

    @Test
    fun verifyTodoItemDisplaysTodoTitle() {
        titleItem.assertIsDisplayed()
        titleItem.assertTextContains(title)
    }

    @Test
    fun verifyTodoItemIsClickable() {
        titleItem.assertHasClickAction()
        titleItem.performClick()
        assertTrue(buttonClicked)
    }

    @Test
    fun verifyTodoItemContainsRightArrow() {
        rightArrow.assertIsDisplayed()
    }

    private fun getRandomString(length: Int): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return List(length) { charset.random() }
            .joinToString("")
    }
}