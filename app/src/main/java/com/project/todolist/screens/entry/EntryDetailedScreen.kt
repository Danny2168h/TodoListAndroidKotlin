package com.project.todolist.screens.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.todolist.R
import com.project.todolist.ui.theme.WhiteBackground
import com.project.todolist.ui.theme.WhiteTextColor
import com.project.todolist.ui.theme.josefinsans
import com.project.todolist.ui.theme.montserrat

@Composable
fun EntryDetailedScreen(
    title: String,
    description: String,
    viewModel: EntryDetailedScreenViewModel
) {
    EntryDetailedScreenMain(
        title = title,
        description = description,
        clickReturn = { viewModel.clickReturn() },
        clickSave = { title, description -> viewModel.clickSave(title, description) })
}

@Composable
fun EntryDetailedScreenMain(
    title: String,
    description: String,
    clickReturn: () -> Unit,
    clickSave: (title: String, description: String) -> Unit,
) {
    var enabledSave by remember { mutableStateOf(false) }
    var enabledChangeTitle by remember { mutableStateOf(false) }
    var titleTextState by remember { mutableStateOf(title) }
    var previousTitleState by remember { mutableStateOf(title) }

    var todoDescriptionState by remember { mutableStateOf(title) }
    var previousDescriptionState by remember { mutableStateOf(description) }

    val back = stringResource(R.string.arrow_back)
    val edit = stringResource(R.string.edit)
    val checkSave = stringResource(R.string.check_save)
    val clear = stringResource(R.string.clear_changes)

    val scroll = rememberScrollState(0)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(0.dp, 20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            if (!enabledChangeTitle) {
                Icon(
                    Icons.Rounded.ArrowBackIosNew,
                    contentDescription = null,
                    tint = WhiteBackground,
                    modifier = Modifier
                        .size(22.dp)
                        .semantics { testTag = back }
                        .clickable { clickReturn() })
            } else {
                Icon(
                    Icons.Rounded.Clear,
                    contentDescription = null,
                    tint = WhiteBackground,
                    modifier = Modifier
                        .size(22.dp)
                        .semantics { testTag = clear }
                        .clickable {
                            titleTextState = previousTitleState
                            todoDescriptionState = previousDescriptionState
                            enabledChangeTitle = false
                        })
            }
            Text(
                text = stringResource(id = R.string.todo_title),
                color = WhiteTextColor,
                fontSize = 25.sp,
                fontFamily = montserrat,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(250.dp)
            )
            if (enabledChangeTitle) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    tint = WhiteBackground,
                    modifier = Modifier
                        .size(22.dp)
                        .semantics { testTag = checkSave }
                        .clickable {
                            if (enabledSave) {
                                clickSave(titleTextState, todoDescriptionState)
                                enabledChangeTitle = false
                            }
                        })
            } else {
                Icon(
                    Icons.Rounded.Edit,
                    contentDescription = null,
                    tint = WhiteBackground,
                    modifier = Modifier
                        .size(22.dp)
                        .semantics { testTag = edit }
                        .clickable {
                            previousTitleState = titleTextState
                            previousDescriptionState = todoDescriptionState
                            enabledChangeTitle = true
                        })
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(350.dp)
                    .border(2.dp, WhiteBackground, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = todoDescriptionState,
                    fontSize = 20.sp,
                    color = WhiteTextColor,
                    modifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 5.dp),
                    textAlign = TextAlign.Center,
                    fontFamily = josefinsans,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}



