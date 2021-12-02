package com.project.todolist

import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.project.todolist.screens.todolist.TopInfoArea
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class TopInfoAreaTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun verifyMainMenuButtonClickable() {
       composeTestRule.setContent {
           TopInfoArea(
               clickMainMenu = { /*Tested Elsewhere*/ }, count = 0
           )
       }
        val mainMenuText = composeTestRule.activity.getString(R.string.main_menu)
        composeTestRule.onNodeWithText(mainMenuText).assertHasClickAction()
    }

    @Test
    fun verifyCounterInitializesProperly() {
        val counts : Int = (0..100).random()
        composeTestRule.setContent {
            TopInfoArea(
                clickMainMenu = { /*Tested Elsewhere*/ }, count = counts
            )
        }
        val tasks = composeTestRule.activity.getString(R.string.tasks)
        composeTestRule.onNodeWithText("$counts " + tasks)
    }
}