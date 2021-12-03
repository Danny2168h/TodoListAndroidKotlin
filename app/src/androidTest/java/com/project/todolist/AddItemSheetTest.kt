package com.project.todolist

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.project.todolist.screens.todolist.ListDetailedScreenMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

class AddItemSheetTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private var clicked = false
    private lateinit var plusSign : SemanticsNodeInteraction
    private lateinit var whatToDo : SemanticsNodeInteraction
    private lateinit var saveButton : SemanticsNodeInteraction


    @ExperimentalComposeUiApi
    @Before
    fun beforeEach() {
        clicked = false
        composeTestRule.setContent {
            ListDetailedScreenMain(
                todoList = mutableListOf(),
                count = 0,
                onClickEntry = {/* Tested elsewhere */},
                onTapSave = {clicked = true}
            )
        }

        plusSign = composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.plus_sign))
        whatToDo =  composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.what_to_do))
        saveButton = composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.save))
    }

    @ExperimentalComposeUiApi
    @Test
    fun verifyBottomSheetsOpenUponTappingFAB() {

        whatToDo.assertIsNotDisplayed()
        saveButton.assertIsNotDisplayed()
        plusSign.assertIsDisplayed()
        plusSign.assertHasClickAction()

        plusSign.performClick()

        whatToDo.assertIsDisplayed()
        saveButton.assertIsDisplayed()
    }

    @ExperimentalComposeUiApi
    @Test
    fun verifySaveButtonIsDisabledWhenThereIsNoText() {
        plusSign.performClick()
        saveButton.assertIsNotEnabled()
        whatToDo.performTextInput("a")
        whatToDo.performTextReplacement("")
        saveButton.assertIsNotEnabled()
    }

    @ExperimentalComposeUiApi
    @Test
    fun verifySaveButtonIsEnabledWhenTextFieldContainsChars() {
        plusSign.performClick()
        whatToDo.performTextInput("a")
        saveButton.assertIsEnabled()
        whatToDo.performTextReplacement("")
        whatToDo.performTextInput("a")
        saveButton.assertIsEnabled()
    }

    @ExperimentalComposeUiApi
    @Test
    fun verifyTappingSaveRetreatsSheet() {
        plusSign.performClick()
        whatToDo.performTextInput("a")
        saveButton.performClick()

        whatToDo.assertIsNotDisplayed()
        saveButton.assertIsNotDisplayed()
    }


}