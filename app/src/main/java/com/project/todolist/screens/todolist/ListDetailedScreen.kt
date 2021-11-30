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
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project.todolist.data.TodoItem
import com.project.todolist.navigation.Screen
import com.project.todolist.testData.ScreenUiData
import com.project.todolist.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListDetailedScreen(navController: NavController, uiData: ScreenUiData) {
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
                    uiData = uiData,
                    navController = navController
                )
            },
        ) {
            Scaffold(
                floatingActionButton = {
                    AddItemButton(scaffoldState = scaffoldState, scope = scope)
                },
                floatingActionButtonPosition = FabPosition.End,
                backgroundColor = WhiteBackground, //This controls background colour
            ) {
                Column(verticalArrangement = Arrangement.spacedBy((-28).dp)) {
                    Box(
                        modifier = Modifier
                            .height(225.dp)
                            .fillMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        DarkerBlue,
                                        LightBlue
                                    )
                                )
                            )
                    ) {
                        BlueTop(clickMainMenu = { /*TODO*/ }, uiData = uiData)
                    }
                    TodoListUI(
                        entries = uiData.listOfTodo,
                        onClickEntry = { navController.navigate(Screen.DetailedView.route + "/$it") })
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddItemButton(scaffoldState: BottomSheetScaffoldState, scope: CoroutineScope) {
    FloatingActionButton(
        onClick = {
            scope.launch {
                scaffoldState.bottomSheetState.apply {
                    if (isCollapsed) expand() else collapse()
                }
            }
        },
        shape = RoundedCornerShape(20),
        backgroundColor = DarkerBlue,
        modifier = Modifier
            .shadow(20.dp, RoundedCornerShape(20))
            .size(60.dp),
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 2.dp,
        ),
        content = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                DarkerBlue,
                                LightBlue
                            )
                        )
                    ), contentAlignment = Alignment.Center
            ) { Text(text = "+", color = WhiteTextColor, fontSize = 30.sp) }
        }
    )
}

@Composable
fun TodoItemUI(entry: TodoItem, onClickEntry: (String) -> Unit) {
    var checked by remember { mutableStateOf(entry.complete) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .shadow(5.dp, RoundedCornerShape(20))
            .size(350.dp, 90.dp)
            .background(
                TodoItemBackGround
            )
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
            .border(2.dp, BlackTextColor, CircleShape)
            .background(if (checked) CheckGreen else TodoItemBackGround))
        {
        }
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = entry.title,
            color = BlackTextColor,
            fontSize = 20.sp,
            modifier = Modifier.width(270.dp),
            fontFamily = dmSans, fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = ">",
            color = BlackTextColor,
            fontSize = 20.sp,
            fontFamily = dmSans,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun TodoListUI(entries: SnapshotStateList<TodoItem>, onClickEntry: (String) -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(30.dp, 0.dp, 0.dp, 0.dp))
            .fillMaxHeight()
            .fillMaxWidth()
            .background(WhiteBackground)
    ) {
        Column {
            Spacer(modifier = Modifier.padding(0.dp, 10.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(15.dp),
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
    uiData: ScreenUiData,
    navController: NavController
) {
    var enabled by remember { mutableStateOf(false) }
    val textState = remember { mutableStateOf(TextFieldValue()) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DarkerBlue,
                        LightBlue
                    )
                )
            )
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
            )
            Spacer(Modifier.padding(0.dp, 15.dp))
            val keyboardController = LocalSoftwareKeyboardController.current
            OutlinedTextField(
                value = textState.value,
                onValueChange =
                {
                    textState.value = it
                    enabled = textState.value.text.isNotEmpty()
                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .border(2.dp, WhiteBackground, RoundedCornerShape(20.dp)),
                singleLine = true,
                placeholder = {
                    Text(
                        text = "What do you need to do?",
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
                    if (enabled) {
                        uiData.addItem(TodoItem(textState.value.text))
                        navController.navigate(route = Screen.ListDetailedView.route)
                    }
                },
                shape = RoundedCornerShape(20),
                colors = ButtonDefaults.buttonColors(WhiteBackground),
                content = {
                    Text(
                        text = "Save",
                        fontFamily = dmSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                })
        }
    }
}

@Composable
fun BlueTop(clickMainMenu: () -> Unit, uiData: ScreenUiData) {
    Column {
        Spacer(modifier = Modifier.padding(0.dp, 30.dp))
        Text(
            text = uiData.dateCurrent,
            fontSize = 18.sp,
            color = WhiteTextColor,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontFamily = dmSans,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.padding(20.dp, 0.dp))
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = "Today",
                    fontSize = 30.sp,
                    color = WhiteTextColor,
                    fontFamily = dmSans,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${uiData.listOfTodo.size} tasks",
                    color = LightGrey,
                    fontSize = 15.sp,
                    fontFamily = dmSans
                )
            }
            Spacer(modifier = Modifier.padding(60.dp, 0.dp))
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(50.dp)
                    .shadow(10.dp, RoundedCornerShape(20))
                    .background(WhiteBackground)
                    .clickable {
                        clickMainMenu()
                    }
            ) {
                Text(
                    text = "Main Menu",
                    Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 15.dp),
                    textAlign = TextAlign.Center,
                    color = DarkerBlue,
                    fontSize = 18.sp,
                    fontFamily = dmSans,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}