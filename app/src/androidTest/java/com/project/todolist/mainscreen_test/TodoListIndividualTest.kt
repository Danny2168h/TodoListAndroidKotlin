package com.project.todolist.mainscreen_test

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.project.todolist.MainActivity
import com.project.todolist.R
import com.project.todolist.model.TodoItem
import com.project.todolist.model.TodoList
import com.project.todolist.screens.main.TodoListIndividual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodoListIndividualTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val title =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum"
    private val todoList =
        TodoList(title = title, todoItems = listOf(TodoItem("1"), TodoItem("2"), TodoItem("3")))
    private lateinit var deleteIcon: SemanticsNodeInteraction
    private lateinit var individualList: SemanticsNodeInteraction
    private lateinit var counter: SemanticsNodeInteraction
    private lateinit var blackBar: SemanticsNodeInteraction

    @Before
    fun beforeEach() {
        composeTestRule.setContent {
            TodoListIndividual(
                todoList = todoList,
                onClickEntry = { arg1, arg2 -> twoParam(0, "") /*tested elsewhere*/ },
                onClickDelete = { /*tested elsewhere*/ }
            )
            deleteIcon = composeTestRule.onNodeWithTag(
                composeTestRule.activity.getString(R.string.close_icon),
                useUnmergedTree = true
            )
            individualList =
                composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.individual_list))
            counter = composeTestRule.onNodeWithTag(
                composeTestRule.activity.getString(R.string.counter),
                useUnmergedTree = true
            )
            blackBar = composeTestRule.onNodeWithTag(
                composeTestRule.activity.getString(R.string.black_bar),
                useUnmergedTree = true
            )
        }
    }

    @Test
    fun verifyTitleDisplayed() {
        individualList.assertTextContains(title)
    }

    @Test
    fun verifyItemCanBeClicked() {
        individualList.assertHasClickAction()
    }

    @Test
    fun verifyCloseIconCanBeTapped() {
        deleteIcon.assertHasClickAction()
    }

    @Test
    fun verifyCounterIsDisplayed() {
        counter.assertTextEquals("${todoList.todoItems.size} items")
    }

    @Test
    fun verifyLongTitleIsScrollable() {
        composeTestRule.onNode(hasScrollAction(), useUnmergedTree = true).assertTextEquals(title)
    }

    @Test
    fun verifyBlackBarVisible() {
        blackBar.assertIsDisplayed()
    }

    private fun twoParam(num: Long, word: String) {
        //Do nothing
    }

}
