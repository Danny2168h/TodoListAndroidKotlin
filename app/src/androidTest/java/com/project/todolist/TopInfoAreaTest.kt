package com.project.todolist

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.project.todolist.screens.todolist.TopInfoArea
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class TopInfoAreaTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private var buttonClicked = false
    private val counts: Int = (0..100).random()

    @Before
    fun setUp() {
        buttonClicked = false
        composeTestRule.setContent {
            TopInfoArea(
                clickMainMenu = { buttonClicked = true }, count = counts
            )
        }
    }

    @Test
    fun verifyMainMenuButtonClickable() {
        val mainMenuText = composeTestRule.activity.getString(R.string.main_menu)
        composeTestRule.onNodeWithText(mainMenuText).assertHasClickAction()
        composeTestRule.onNodeWithText(mainMenuText).performClick()
        assertTrue(buttonClicked)
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
    fun verifyTodayIsDisplayed() {
        val todayText = composeTestRule.activity.getString(R.string.today)
        composeTestRule.onNodeWithText(todayText).assertIsDisplayed()
    }
}