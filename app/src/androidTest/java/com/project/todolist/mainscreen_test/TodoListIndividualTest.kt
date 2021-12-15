package com.project.todolist.mainscreen_test

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.project.todolist.MainActivity
import com.project.todolist.R
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

    private val title = "Testing Todo"
    private val todoList = TodoList(title = title)
    private lateinit var deleteIcon: SemanticsNodeInteraction
    private lateinit var individualList: SemanticsNodeInteraction

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

    private fun twoParam(num: Long, word: String) {
        //Do nothing
    }

}