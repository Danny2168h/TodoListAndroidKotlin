package com.project.todolist.mainscreen_test

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.project.todolist.MainActivity
import com.project.todolist.R
import com.project.todolist.data.TodoList
import com.project.todolist.screens.main.MainScreenMain
import com.project.todolist.screens.main.TitleArea
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddListSheetTest {

    private val title = "Testing Todo"
    private val todoLists = listOf(TodoList(title = title))
    private lateinit var addListButton : SemanticsNodeInteraction
    private lateinit var todoTitleTextArea : SemanticsNodeInteraction
    private lateinit var createButton : SemanticsNodeInteraction
    private lateinit var addIcon : SemanticsNodeInteraction

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @Before
    fun beforeEach() {
        composeTestRule.setContent {
            MainScreenMain(
                todoLists = todoLists,
                onClickEntry = { /*tested elsewhere*/ },
                onTapSave = { /*tested elsewhere*/ },
                onClickDelete = { /*tested elsewhere*/ })
        }
        addListButton = composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.list))
        todoTitleTextArea = composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.todo_list_title))
        createButton = composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.create))
        addIcon = composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.add_icon), useUnmergedTree = true)
    }

    @Test
    fun verifyListButtonUI() {
        addListButton.assertIsDisplayed()
            .assertTextContains(composeTestRule.activity.getString(R.string.list))
            .assertHasClickAction()
        addIcon.assertIsDisplayed()
    }

    @Test
    fun verifyBottomSheetOpenAfterClickingButton() {
        todoTitleTextArea.assertIsNotDisplayed()
        createButton.assertIsNotDisplayed()

        addListButton.performClick()

        todoTitleTextArea.assertIsDisplayed()
        createButton.assertIsDisplayed()
    }

    @Test
    fun verifyCreateButtonIsDisabledWhenThereIsNoText() {
        addListButton.performClick()

        createButton.assertIsNotEnabled()

        todoTitleTextArea.performTextInput("abc123")
        todoTitleTextArea.performTextClearance()

        createButton.assertIsNotEnabled()
    }

    @Test
    fun verifyCreateButtonIsEnabledWhenThereIsText() {
        addListButton.performClick()

        todoTitleTextArea.performTextInput("abc123")
        createButton.assertIsEnabled()

        todoTitleTextArea.performTextClearance()
        todoTitleTextArea.performTextInput("abc123")

        createButton.assertIsEnabled()
    }

    @Test
    fun verifyTappingCreateRetreatsSheet() {
        addListButton.performClick()

        todoTitleTextArea.performTextInput("abc123")

        createButton.performClick()

        todoTitleTextArea.assertIsNotDisplayed()
        createButton.assertIsNotDisplayed()
    }

}