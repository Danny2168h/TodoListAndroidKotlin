package com.project.todolist.mainscreen_test

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.project.todolist.MainActivity
import com.project.todolist.R
import com.project.todolist.screens.main.TitleArea
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TitleAreaTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun verifyTitleDisplayed() {
        composeTestRule.setContent {
            TitleArea()
        }
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.todo_title))
    }

}