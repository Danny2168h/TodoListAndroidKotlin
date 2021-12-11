package com.project.todolist.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project.todolist.R
import com.project.todolist.data.TodoList
import com.project.todolist.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun MainScreen(navController: NavController) {

    val viewModel = MainScreenViewModel(navController = navController)
    val state by viewModel.state.collectAsState()

    MainScreenMain(
        todoLists = state.todoLists,
        onClickEntry = { arg1, arg2 -> viewModel.onTapEntry(arg1, arg2) },
        onTapSave = { viewModel.onTapSave(it) },
        onClickDelete = { viewModel.onTapDelete(it) })
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun MainScreenMain(
    todoLists: List<TodoList>,
    onClickEntry: (Long, String) -> Unit,
    onTapSave: (String) -> Unit,
    onClickDelete: (Long) -> Unit
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
                AddListUI(
                    OnTapSave = {
                        onTapSave(it)
                    },
                    scope = scope,
                    scaffoldState = scaffoldState
                )
            },
        ) {
            Scaffold(
                bottomBar = {
                    AddListButton(scaffoldState = scaffoldState, scope = scope)
                },
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mountains_darkened),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.matchParentSize()
                    )
                    Column {
                        TitleArea()
                        TodoListSelection(
                            todoLists = todoLists,
                            onClickEntry = { arg1, arg2 -> onClickEntry(arg1, arg2) },
                            onClickDelete = { onClickDelete(it) })
                    }
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun AddListUI(
    OnTapSave: (String) -> Unit,
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState
) {
    var enabled by remember { mutableStateOf(false) }
    val textState = remember { mutableStateOf(TextFieldValue()) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(BlackAddList)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
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
                        text = stringResource(id = R.string.todo_list_title),
                        color = WhiteTextColor,
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
                    OnTapSave(textState.value.text)
                    scope.launch {
                        scaffoldState.bottomSheetState.apply {
                            if (isCollapsed) expand() else collapse()
                        }
                    }

                },
                enabled = enabled,
                shape = RoundedCornerShape(20),
                colors = ButtonDefaults.buttonColors(WhiteBackground),
                content = {
                    Text(
                        text = stringResource(id = R.string.create),
                        fontFamily = dmSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                })
        }
    }
}

@Composable
fun TitleArea() {
    Column(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(20.dp))
        Text(
            text = stringResource(id = R.string.todo_title),
            color = WhiteTextColor,
            fontSize = 25.sp,
            fontFamily = montserrat,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun TodoListSelection(
    todoLists: List<TodoList>,
    onClickEntry: (Long, String) -> Unit,
    onClickDelete: (Long) -> Unit
) {
    val pairedLists = separateInto2s(todoLists)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(35.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                items(pairedLists) { todoList ->
                    TodoListPairRow(
                        todoList,
                        onClickEntry = { arg1, arg2 -> onClickEntry(arg1, arg2) },
                        onClickDelete = { onClickDelete(it) })
                }
            }
        }
    }
}

@Composable
fun TodoListPairRow(
    pairTodoList: Pair<TodoList, TodoList?>,
    onClickEntry: (Long, String) -> Unit,
    onClickDelete: (Long) -> Unit
) {
    val pairRows = stringResource(R.string.pair_rows)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.semantics { testTag = pairRows }) {
        TodoListIndividual(
            todoList = pairTodoList.first,
            onClickEntry = { arg1, arg2 -> onClickEntry(arg1, arg2) },
            onClickDelete = { onClickDelete(it) })
        if (pairTodoList.second != null) {
            Spacer(modifier = Modifier.padding(15.dp, 0.dp))
            TodoListIndividual(
                todoList = pairTodoList.second!!,
                onClickEntry = { arg1, arg2 -> onClickEntry(arg1, arg2) },
                onClickDelete = { onClickDelete(it) })
        }
    }
}

@Composable
fun TodoListIndividual(
    todoList: TodoList,
    onClickEntry: (id: Long, title: String) -> Unit,
    onClickDelete: (Long) -> Unit
) {
    val closeIcon = stringResource(R.string.close_icon)
    val individualList = stringResource(R.string.individual_list)
    Box(modifier = Modifier
        .shadow(2.dp, RoundedCornerShape(20))
        .size(160.dp, 180.dp)
        .clickable { onClickEntry(todoList.id, todoList.title) }
        .background(WhiteTextColor)
        .semantics { testTag = individualList }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.padding(vertical = 5.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Icon(
                    Icons.Rounded.Close,
                    contentDescription = null,
                    tint = BlackTextColor,
                    modifier = Modifier
                        .size(22.dp)
                        .clickable { onClickDelete(todoList.id) }
                        .semantics { testTag = closeIcon }
                )
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            }
            Spacer(modifier = Modifier.padding(0.dp, 0.dp))
            Text(
                text = todoList.title,
                fontFamily = montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = BlackTextColor,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

    }
}

@ExperimentalMaterialApi
@Composable
fun AddListButton(
    scaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope
) {
    val addIcon = stringResource(R.string.add_icon)
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
    {
        Box(
            modifier = Modifier
                .shadow(20.dp, RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp))
                .background(BlackAddList)
                .width(120.dp)
                .height(40.dp)
                .clickable {
                    scope.launch {
                        scaffoldState.bottomSheetState.apply {
                            if (isCollapsed) expand() else collapse()
                        }
                    }
                }, contentAlignment = Alignment.Center
        ) {
            Row {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = null,
                    tint = WhiteTextColor,
                    modifier = Modifier.semantics { testTag = addIcon })
                PaddingValues(10.dp, 0.dp)
                Text(
                    text = stringResource(id = R.string.list),
                    color = WhiteTextColor,
                    fontFamily = dmSans,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun separateInto2s(todoLists: List<TodoList>): List<Pair<TodoList, TodoList?>> {
    var output = mutableListOf<Pair<TodoList, TodoList?>>()
    var listLength = todoLists.size
    var odd = false

    if (listLength % 2 != 0) {
        listLength -= 1
        odd = true
    }

    var currIndex = 0
    while (currIndex < listLength) {
        output.add(Pair(todoLists[currIndex], todoLists[currIndex + 1]))
        currIndex += 2
    }

    if (odd) {
        output.add(Pair(todoLists[listLength], null))
    }

    return output
}





