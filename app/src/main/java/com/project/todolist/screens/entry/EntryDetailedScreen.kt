package com.project.todolist.screens.entry

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.todolist.R
import com.project.todolist.ui.theme.*

@ExperimentalComposeUiApi
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
        clickSave = { title, description -> viewModel.clickSave(title, description) },
        clickDelete = { viewModel.clickDelete()}
    )
}

@ExperimentalComposeUiApi
@Composable
fun EntryDetailedScreenMain(
    title: String,
    description: String,
    clickReturn: () -> Unit,
    clickSave: (title: String, description: String) -> Unit,
    clickDelete: () -> Unit
) {
    val back = stringResource(R.string.arrow_back)
    val edit = stringResource(R.string.edit)
    val checkSave = stringResource(R.string.check_save)
    val clear = stringResource(R.string.clear_changes)
    val addDesc = stringResource(id = R.string.add_description_toTodo)

    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollTitle = rememberScrollState()
    val scrollDescription = rememberScrollState()
    var titleNotEmpty by remember { mutableStateOf(true) }
    var enabledChangeTitle by remember { mutableStateOf(false) }
    var titleTextState by remember { mutableStateOf(title) }
    var previousTitleState by remember { mutableStateOf(title) }
    var todoDescriptionState by remember { mutableStateOf(description.trim()) }

    var previousDescriptionState by remember { mutableStateOf(description.trim()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
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
                            if (titleNotEmpty) {
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
        Spacer(modifier = Modifier.padding(0.dp, 20.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!enabledChangeTitle) {
                Box(
                    modifier = Modifier
                        .width(350.dp)
                        .sizeIn(maxHeight = 100.dp)
                        .border(2.dp, WhiteBackground, RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = titleTextState,
                        fontSize = 20.sp,
                        color = WhiteTextColor,
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 5.dp)
                            .verticalScroll(scrollTitle),
                        textAlign = TextAlign.Center,
                        fontFamily = josefinsans,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.padding(0.dp, 10.dp))
                Box(
                    modifier = Modifier
                        .width(350.dp)
                        .sizeIn(minHeight = 300.dp, maxHeight = 450.dp)
                        .border(2.dp, WhiteBackground, RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (todoDescriptionState.isEmpty()) {addDesc} else {todoDescriptionState},
                        fontSize = 20.sp,
                        color = WhiteTextColor,
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 5.dp)
                            .verticalScroll(scrollDescription),
                        textAlign = TextAlign.Center,
                        fontFamily = josefinsans,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                OutlinedTextField(
                    value = titleTextState,
                    onValueChange =
                    {
                        titleTextState = it
                        titleNotEmpty = titleTextState.isNotEmpty()
                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .width(350.dp)
                        .sizeIn(maxHeight = 100.dp)
                        .border(2.dp, WhiteBackground, RoundedCornerShape(20.dp)),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.new_itemTitle),
                            color = WhiteTextColorFade,
                            textAlign = TextAlign.Center,
                            fontFamily = dmSans,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        color = WhiteTextColor,
                        fontFamily = josefinsans,
                        fontWeight = FontWeight.Bold
                    ),
                    singleLine = false,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                    colors = TextFieldDefaults.textFieldColors(textColor = WhiteTextColor),
                )
                Spacer(modifier = Modifier.padding(0.dp, 10.dp))
                OutlinedTextField(
                    value = todoDescriptionState,
                    onValueChange =
                    {
                        todoDescriptionState = it
                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .width(350.dp)
                        .sizeIn(minHeight = 300.dp, maxHeight = 450.dp)
                        .border(2.dp, WhiteBackground, RoundedCornerShape(20.dp)),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.add_description_toTodo),
                            color = WhiteTextColorFade,
                            textAlign = TextAlign.Start,
                            fontFamily = dmSans,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        color = WhiteTextColor,
                        fontFamily = josefinsans,
                        fontWeight = FontWeight.Bold
                    ),
                    singleLine = false,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                    colors = TextFieldDefaults.textFieldColors(textColor = WhiteTextColor),
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            DeleteItemButton(clickDelete = { clickDelete() })
        }
    }
}

@Composable
fun DeleteItemButton(
    clickDelete: () -> Unit) {
    val trashIcon = stringResource(R.string.delete_forever)
    Box(
        modifier = Modifier
            .shadow(20.dp, RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp))
            .background(Color.Red)
            .clickable { clickDelete() },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)) {
            Icon(
                Icons.Rounded.DeleteForever,
                contentDescription = null,
                tint = WhiteTextColor,
                modifier = Modifier.semantics { testTag = trashIcon })
            PaddingValues(10.dp, 0.dp)
            Text(
                text = stringResource(id = R.string.delete),
                color = WhiteTextColor,
                fontFamily = dmSans,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}



