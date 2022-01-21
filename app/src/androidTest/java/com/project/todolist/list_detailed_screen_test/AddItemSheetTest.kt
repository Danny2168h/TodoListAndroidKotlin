package com.project.todolist.list_detailed_screen_test

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.project.todolist.MainActivity
import com.project.todolist.R
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
    private lateinit var plusSign: SemanticsNodeInteraction
    private lateinit var whatToDo: SemanticsNodeInteraction
    private lateinit var saveButton: SemanticsNodeInteraction
    private lateinit var topBox: SemanticsNodeInteraction


    @ExperimentalComposeUiApi
    @Before
    fun beforeEach() {
        clicked = false
        composeTestRule.setContent {
            ListDetailedScreenMain(
                todoList = mutableListOf(),
                count = 0,
                onClickEntry = { arg1, arg2, arg3 -> /* Tested elsewhere */ },
                onTapSave = { arg1, arg2 -> /* Tested elsewhere */ },
                todoTitle = "",
                onClickCompleted = {/* Tested elsewhere */ },
                onClickMainMenu = {/* Tested elsewhere */ },
                onClickCheck = {/* Tested elsewhere */ },
                onTapEntryComplete = {/* Tested elsewhere */ },
            )
        }

        plusSign =
            composeTestRule.onNodeWithTag(
                composeTestRule.activity.getString(R.string.plus_sign),
                useUnmergedTree = true
            )
        whatToDo =
            composeTestRule.onNodeWithTag(
                composeTestRule.activity.getString(R.string.what_to_do),
                useUnmergedTree = true
            )
        saveButton =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.save))
        topBox = composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.top_box))
    }

    @ExperimentalComposeUiApi
    @Test
    fun verifyBottomSheetsOpenUponTappingFAB() {

        whatToDo.assertIsNotDisplayed()
        saveButton.assertIsNotDisplayed()
        plusSign.assertIsDisplayed()

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

    //Will no longer work until unit tests can be written for material UI components

//    @ExperimentalComposeUiApi
//    @Test
//    fun verifySaveButtonIsEnabledWhenTextFieldContainsChars() {
//        plusSign.performClick()
//        whatToDo.performTextInput("a")
//        saveButton.assertIsEnabled()
//        whatToDo.performTextReplacement("")
//        whatToDo.performTextInput("a")
//        saveButton.assertIsEnabled()
//    }

    //Will no longer work until unit tests can be written for material UI components

//    @ExperimentalComposeUiApi
//    @Test
//    fun verifyTappingSaveRetreatsSheet() {
//        plusSign.performClick()
//        whatToDo.performTextInput("a")
//        saveButton.performClick()
//
//        whatToDo.assertIsNotDisplayed()
//        saveButton.assertIsNotDisplayed()
//    }


    //Will no longer work until unit tests can be written for material UI components

//    @Test
//    fun verifyTextIsClearedAfterSaving() {
//        plusSign.performClick()
//        whatToDo.performTextInput("a")
//        saveButton.performClick()
//
//        plusSign.performClick()
//        whatToDo.assert(hasText(""))
//    }

}