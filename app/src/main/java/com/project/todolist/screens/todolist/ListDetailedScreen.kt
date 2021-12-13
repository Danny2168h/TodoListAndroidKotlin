package com.project.todolist.screens.todolist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.project.todolist.data.TodoItem
import com.project.todolist.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListDetailedScreen(viewModel: ListDetailedScreenViewModel, todoTitle: String) {
    val state by viewModel.state.collectAsState()

    ListDetailedScreenMain(
        todoList = state.todoList,
        count = state.count,
        todoTitle = todoTitle,
        onClickEntry = { viewModel.onTapEntry(it) },
        onTapSave = { viewModel.onTapSave(it) },
        onClickMainMenu = { viewModel.onClickMainMenu() },
        onClickCompleted = { viewModel.onClickCompleted() },
        onClickCheck = { viewModel.onClickCheck(it) },
    )
}

@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListDetailedScreenMain(
    onTapSave: (String) -> Unit,
    todoList: List<TodoItem>,
    count: Int,
    todoTitle: String,
    onClickEntry: (String) -> Unit,
    onClickMainMenu: () -> Unit,
    onClickCompleted: () -> Unit,
    onClickCheck: (String) -> Unit,
) {
    TodoListTheme {
        val scaffoldState = rememberBottomSheetScaffoldState()
        val scope = rememberCoroutineScope()
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = 0.dp,
            sheetShape = RoundedCornerShape(30.dp, 30.dp, 0.dp, 0.dp),
            sheetElevation = 10.dp,
            sheetContent = {
                AddItemUI(
                    OnTapSave = { onTapSave(it) },
                    scope = scope,
                    scaffoldState = scaffoldState
                )
            },
        ) {
            Scaffold(
                floatingActionButton = {
                    AddItemButton(scaffoldState = scaffoldState, scope = scope)
                },
                floatingActionButtonPosition = FabPosition.End,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy((-28).dp)) {
                    Box(
                        modifier = Modifier
                            .height(270.dp)
                            .fillMaxWidth()
                            .background(ListDetailedTopInfoArea),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        TopInfoArea(
                            clickMainMenu = { onClickMainMenu() },
                            onClickCheckSave = { onClickCheck(it) },
                            onClickCompleted = { onClickCompleted() },
                            count = count,
                            todoTitle = todoTitle
                        )
                    }
                    TodoListUI(
                        entries = todoList,
                        onClickEntry = { onClickEntry(it) })
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddItemButton(
    scaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope
) {
    FloatingActionButton(
        onClick = {
            scope.launch {
                scaffoldState.bottomSheetState.apply {
                    if (isCollapsed) expand() else collapse()
                }
            }
        },
        shape = RoundedCornerShape(35),
        modifier = Modifier
            .shadow(20.dp, RoundedCornerShape(35))
            .size(60.dp),
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 2.dp,
        ),
        content = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(DarkerBlue),
                contentAlignment = Alignment.Center
            ) {
                val plus = stringResource(R.string.plus_sign)
                Icon(Icons.Rounded.AddCircle,
                    contentDescription = null,
                    tint = WhiteBackground,
                    modifier = Modifier
                        .size(28.dp)
                        .semantics { testTag = plus })
            }
        }
    )
}

@Composable
fun TodoItemUI(
    entry: TodoItem,
    onClickEntry: (String) -> Unit
) {
    var checked by remember { mutableStateOf(entry.complete) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .shadow(2.dp, RoundedCornerShape(20))
            .size(350.dp, 90.dp)
            .background(TodoItemBackGround)
            .clickable { onClickEntry(entry.title) }
    ) {
        Spacer(modifier = Modifier.padding(10.dp))
        Box(modifier = Modifier
            .clickable {
                checked = !checked
                entry.complete = checked
            }
            .clip(CircleShape)
            .size(20.dp)
            .border(2.dp, WhiteTextColor, CircleShape)
            .background(if (checked) CheckGreen else TodoItemBackGround))
        {
        }
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = entry.title,
            color = WhiteTextColor,
            fontSize = 20.sp,
            modifier = Modifier.width(255.dp),
            fontFamily = dmSans, fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.padding(5.dp))
        val arrowRight = stringResource(R.string.arrow_right)
        Icon(
            Icons.Rounded.ArrowForwardIos,
            "",
            modifier = Modifier
                .semantics { testTag = arrowRight }
                .size(18.dp),
            tint = WhiteTextColor
        )
    }
}


@Composable
fun TodoListUI(
    entries: List<TodoItem>,
    onClickEntry: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(0.dp, 30.dp, 0.dp, 0.dp))
            .fillMaxHeight()
            .fillMaxWidth()
            .background(ListDetailedViewBackGround)
    ) {
        Column {
            Spacer(modifier = Modifier.padding(0.dp, 10.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            )
            {
                items(entries) { entry ->
                    TodoItemUI(entry) {
                        onClickEntry(it)
                    }
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddItemUI(
    OnTapSave: (String) -> Unit,
    scaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope
) {
    var enabled by remember { mutableStateOf(false) }
    var textState by remember { mutableStateOf("") }
    val topBox = stringResource(R.string.top_box)
    val needToDo = stringResource(id = R.string.what_to_do)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(DarkerBlue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.padding(0.dp, 7.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .width(35.dp)
                    .height(5.dp)
                    .background(WhiteBackground)
                    .semantics { testTag = topBox }
            )
            Spacer(Modifier.padding(0.dp, 15.dp))
            val keyboardController = LocalSoftwareKeyboardController.current
            OutlinedTextField(
                value = textState,
                onValueChange =
                {
                    textState = it
                    enabled = textState.isNotEmpty()
                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .border(2.dp, WhiteBackground, RoundedCornerShape(20.dp))
                    .semantics { testTag = needToDo },
                singleLine = true,
                placeholder = {
                    Text(
                        text = needToDo,
                        color = WhiteTextColorFade,
                        textAlign = TextAlign.Center,
                        fontFamily = dmSans,
                        fontWeight = FontWeight.Bold
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(textColor = WhiteTextColor),
            )
            Spacer(modifier = Modifier.padding(0.dp, 15.dp))
            Button(
                onClick = {
                    OnTapSave(textState)
                    scope.launch {
                        scaffoldState.bottomSheetState.apply {
                            if (isCollapsed) expand() else collapse()
                        }
                    }
                    textState = ""
                },
                enabled = enabled,
                shape = RoundedCornerShape(20),
                colors = ButtonDefaults.buttonColors(WhiteBackground),
                content = {
                    Text(
                        text = stringResource(id = R.string.save),
                        fontFamily = dmSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                })
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun TopInfoArea(
    clickMainMenu: () -> Unit,
    onClickCompleted: () -> Unit,
    onClickCheckSave: (String) -> Unit,
    count: Int,
    todoTitle: String
) {
    val home = stringResource(R.string.home_icon)
    val edit = stringResource(R.string.edit)
    val checkSave = stringResource(R.string.check_save)
    val clear = stringResource(R.string.clear_changes)

    var enabledSave by remember { mutableStateOf(false) }
    var enabledChangeTitle by remember { mutableStateOf(false) }
    var textState by remember { mutableStateOf(todoTitle) }
    var previousTextState by remember { mutableStateOf(todoTitle) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.padding(0.dp, 20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            if (!enabledChangeTitle) {
                Icon(Icons.Rounded.Home,
                    contentDescription = null,
                    tint = WhiteBackground,
                    modifier = Modifier
                        .size(22.dp)
                        .semantics { testTag = home }
                        .clickable { clickMainMenu() })
            } else {
                Icon(Icons.Rounded.Clear,
                    contentDescription = null,
                    tint = WhiteBackground,
                    modifier = Modifier
                        .size(22.dp)
                        .semantics { testTag = clear }
                        .clickable {
                            textState = previousTextState
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
                modifier = Modifier.width(292.dp)
            )
            if (enabledChangeTitle) {
                Icon(Icons.Rounded.Check,
                    contentDescription = null,
                    tint = WhiteBackground,
                    modifier = Modifier
                        .size(22.dp)
                        .semantics { testTag = checkSave }
                        .clickable {
                            if (enabledSave) {
                                onClickCheckSave(textState)
                                enabledChangeTitle = false
                            }
                        })
            } else {
                Icon(Icons.Rounded.Edit,
                    contentDescription = null,
                    tint = WhiteBackground,
                    modifier = Modifier
                        .size(22.dp)
                        .semantics { testTag = edit }
                        .clickable {
                            previousTextState = textState
                            enabledChangeTitle = true
                        })
            }
        }
        Spacer(modifier = Modifier.padding(0.dp, 10.dp))
        if (!enabledChangeTitle) {
            Box(
                modifier = Modifier
                    .width(350.dp)
                    .height(50.dp)
                    .border(2.dp, WhiteBackground, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = textState,
                    fontSize = 20.sp,
                    color = WhiteTextColor,
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    textAlign = TextAlign.Center,
                    fontFamily = josefinsans,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            OutlinedTextField(
                value = textState,
                onValueChange =
                {
                    textState = it
                    enabledSave = textState.isNotEmpty()
                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .width(350.dp)
                    .height(50.dp)
                    .border(2.dp, WhiteBackground, RoundedCornerShape(20.dp)),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.new_todoTitle),
                        color = WhiteTextColorFade,
                        textAlign = TextAlign.Center,
                        fontFamily = dmSans,
                        fontWeight = FontWeight.Bold
                    )
                },
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    color = WhiteTextColor,
                    fontFamily = josefinsans,
                    fontWeight = FontWeight.Bold
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(textColor = WhiteTextColor),
            )
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.padding(10.dp, 0.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = Calendar.getInstance().time.toString().substring(0, 10),
                    fontSize = 20.sp,
                    color = WhiteTextColor,
                    fontFamily = josefinsans,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$count " + stringResource(id = R.string.tasks),
                    color = LightGrey,
                    fontSize = 15.sp,
                    fontFamily = dmSans
                )
            }
            Spacer(modifier = Modifier.padding(53.dp, 0.dp))
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(50.dp)
                    .shadow(2.dp, RoundedCornerShape(20))
                    .background(WhiteBackground)
                    .clickable { onClickCompleted() }
            ) {
                Text(
                    text = stringResource(id = R.string.completed),
                    Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 15.dp),
                    textAlign = TextAlign.Center,
                    color = ListDetailedViewBackGround,
                    fontSize = 18.sp,
                    fontFamily = dmSans,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}