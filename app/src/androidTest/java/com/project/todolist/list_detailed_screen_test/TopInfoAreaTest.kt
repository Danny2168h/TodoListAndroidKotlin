package com.project.todolist.list_detailed_screen_test

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.project.todolist.MainActivity
import com.project.todolist.R
import com.project.todolist.screens.todolist.TopInfoArea
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class TopInfoAreaTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val counts: Int = (0..100).random()
    private lateinit var randomString: String

    private lateinit var homeIcon: SemanticsNodeInteraction
    private lateinit var editIcon: SemanticsNodeInteraction
    private lateinit var clearIcon: SemanticsNodeInteraction
    private lateinit var checkIcon: SemanticsNodeInteraction
    private lateinit var completedButton: SemanticsNodeInteraction


    @ExperimentalComposeUiApi
    @Before
    fun setUp() {
        randomString = getRandomString(15)
        composeTestRule.setContent {
            TopInfoArea(
                clickMainMenu = { }, todoTitle = randomString, count = counts,
                onClickCompleted = {/* Tested Elsewhere */ },
                onClickCheckSave = {/* Tested Elsewhere */ }
            )
        }
        homeIcon =
            composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.home_icon))
        editIcon = composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.edit))
        clearIcon = composeTestRule.onNodeWithTag(
            composeTestRule.activity.getString(R.string.clear_changes),
            useUnmergedTree = true
        )
        checkIcon = composeTestRule.onNodeWithTag(
            composeTestRule.activity.getString(R.string.check_save),
            useUnmergedTree = true
        )
        completedButton =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.completed))
    }

    @Test
    fun verifyCompletedButton() {
        val completedText = composeTestRule.activity.getString(R.string.completed)
        completedButton.assertHasClickAction().assertTextEquals(completedText)
    }

    @Test
    fun verifyCounterInitializesProperly() {
        val tasks = composeTestRule.activity.getString(R.string.tasks)
        composeTestRule.onNodeWithText("$counts $tasks").assertIsDisplayed()
    }

    @Test
    fun verifyDateIsDisplayed() {
        val currDate = Calendar.getInstance().getTime().toString().substring(0, 10)
        composeTestRule.onNodeWithText(currDate).assertIsDisplayed()
    }

    @Test
    fun verifyTodoTitleIsDisplayedProperlyAndNotEditable() {
        composeTestRule.onNodeWithText(randomString)
            .assertIsDisplayed()
            .assertTextEquals(randomString)
            .assertHasNoClickAction()
    }

    @Test
    fun verifyInitialHomeAndEditIcon() {
        homeIcon.assertIsDisplayed().assertHasClickAction()
        editIcon.assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun verifyInitialHomeAndEditIconAreReplacedUponTappingEdit() {
        clearIcon.assertDoesNotExist()
        checkIcon.assertDoesNotExist()
        homeIcon.assertIsDisplayed()
        editIcon.assertIsDisplayed()

        editIcon.performClick()

        clearIcon =
            composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.clear_changes))
        checkIcon =
            composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.check_save))

        homeIcon.assertDoesNotExist()
        editIcon.assertDoesNotExist()
        checkIcon.assertIsDisplayed().assertHasClickAction()
        clearIcon.assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun verifyTitleIsPresentInTextEditField() {
        composeTestRule.onNodeWithText(randomString).assertTextEquals(randomString)
            .assertHasNoClickAction()
        editIcon.performClick()
        composeTestRule.onNodeWithText(randomString).assertTextEquals(randomString)
            .assertHasClickAction()
    }

    @Test
    fun verifyTappingEditAllowsTitleToBeEdited() {
        editIcon.performClick()
        val replacementString = getRandomString(10)
        composeTestRule.onNodeWithText(randomString)
            .assertHasClickAction()
            .performTextReplacement(replacementString)
        composeTestRule.onNodeWithText(replacementString).assertTextEquals(replacementString)
    }

    @Test
    fun verifyChangesAreDiscardedUponTappingCancel() {
        editIcon.performClick()
        val replacementString = getRandomString(10)
        composeTestRule.onNodeWithText(randomString).performTextReplacement(replacementString)
        composeTestRule.onNodeWithText(replacementString).assertTextEquals(replacementString)
        clearIcon =
            composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.clear_changes))
        clearIcon.performClick()
        composeTestRule.onNodeWithText(randomString).assertTextEquals(randomString)
    }

    @Test
    fun verifyUserIsNotAllowedToSaveEmptyTitle() {
        editIcon.performClick()
        composeTestRule.onNodeWithText(randomString).performTextReplacement("")
        checkIcon =
            composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.check_save))
        clearIcon =
            composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.clear_changes))

        checkIcon.performClick()

        checkIcon.assertIsDisplayed()
        clearIcon.assertIsDisplayed()
        homeIcon.assertDoesNotExist()
        editIcon.assertDoesNotExist()
    }

    @Test
    fun verifyTitleIsUpdatedWhenTappingCheckIcon() {
        composeTestRule.onNodeWithText(randomString)
            .assertTextEquals(randomString)

        editIcon.performClick()

        val replacementString = getRandomString(10)
        composeTestRule.onNodeWithText(randomString)
            .performTextReplacement(replacementString)
        checkIcon =
            composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.check_save))
        clearIcon =
            composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.clear_changes))

        checkIcon.performClick()

        homeIcon =
            composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.home_icon))
        editIcon = composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.edit))

        homeIcon.assertIsDisplayed()
        editIcon.assertIsDisplayed()
        checkIcon.assertDoesNotExist()
        clearIcon.assertDoesNotExist()

        composeTestRule.onNodeWithText(replacementString).assertTextEquals(replacementString)
    }

    private fun getRandomString(length: Int): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return List(length) { charset.random() }
            .joinToString("")
    }
}