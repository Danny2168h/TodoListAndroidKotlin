package com.project.todolist

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.project.todolist.data.TodoItem
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

    private val title = "Test Item String"
    private val rightChevron = ">"
    private var buttonClicked = false

    lateinit var titleItem: SemanticsNodeInteraction

    @Before
    fun setup() {
        buttonClicked = false
        composeTestRule.setContent {
            TodoItemUI(
                onClickEntry = { buttonClicked = true }, entry = TodoItem(title)
            )
        }
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
    fun verifyTodoItemContainsRightChevron() {
        titleItem.assertTextContains(rightChevron)
    }
}