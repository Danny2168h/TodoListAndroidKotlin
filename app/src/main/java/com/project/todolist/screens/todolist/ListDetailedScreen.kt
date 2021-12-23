package com.project.todolist.screens.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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
import com.project.todolist.model.IntToMonth
import com.project.todolist.model.TodoItem
import com.project.todolist.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
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
        onClickEntry = { title, description, id ->
            viewModel.onTapEntry(
                title = title,
                description = description,
                id = id
            )
        },
        onTapSave = { itemTitle, itemDueTime -> viewModel.onTapSave(itemTitle, itemDueTime) },
        onClickMainMenu = { viewModel.onClickMainMenu() },
        onClickCompleted = { viewModel.onClickCompleted() },
        onClickCheck = { viewModel.onClickCheck(it) },
        onTapEntryComplete = { viewModel.onTapEntryComplete(it) }
    )
}

@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListDetailedScreenMain(
    onTapSave: (String, String) -> Unit,
    todoList: List<TodoItem>,
    count: Int,
    todoTitle: String,
    onClickEntry: (String, String, String) -> Unit,
    onClickMainMenu: () -> Unit,
    onClickCompleted: () -> Unit,
    onClickCheck: (String) -> Unit,
    onTapEntryComplete: (TodoItem) -> Unit,
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
                    onTapSave = { itemTitle, itemDueTime -> onTapSave(itemTitle, itemDueTime) },
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
                        onClickEntry = { title, description, id ->
                            onClickEntry(
                                title,
                                description,
                                id
                            )
                        },
                        onTapEntryComplete = { onTapEntryComplete(it) })
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
    onClickEntry: (String, String, String) -> Unit,
    onTapEntryComplete: (TodoItem) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp)
            .shadow(2.dp, RoundedCornerShape(20))
            .fillMaxWidth()
            .sizeIn(minHeight = 100.dp)
            .background(TodoItemBackGround)
            .clickable { onClickEntry(entry.title, entry.description, entry.uniqueID) }
    ) {
        Spacer(modifier = Modifier.padding(10.dp))
        Box(modifier = Modifier
            .clickable {
                onTapEntryComplete(entry)
            }
            .clip(CircleShape)
            .size(20.dp)
            .border(2.dp, WhiteTextColor, CircleShape)
            .background(if (entry.markedForCompletion) CheckGreen else TodoItemBackGround)) {}
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = entry.title,
            fontFamily = montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = WhiteTextColor,
            modifier = Modifier
                .width(270.dp)
                .padding(vertical = 10.dp)
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
    onClickEntry: (String, String, String) -> Unit,
    onTapEntryComplete: (TodoItem) -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(25.dp, 25.dp, 0.dp, 0.dp))
            .fillMaxWidth()
            .background(ListDetailedViewBackGround)
    ) {
        Column {
            Spacer(modifier = Modifier.padding(0.dp, 10.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
            {
                items(entries) { entry ->
                    TodoItemUI(entry,
                        onClickEntry = { title, description, id ->
                            onClickEntry(
                                title,
                                description,
                                id
                            )
                        },
                        onTapEntryComplete = { onTapEntryComplete(it) })
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddItemUI(
    onTapSave: (String, String) -> Unit,
    scaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope
) {
    val appContext = LocalContext.current
    var c = Calendar.getInstance()
    val minTimeAhead = 1

    var minuteSelected by remember { mutableStateOf(-1) }
    var hourSelected by remember { mutableStateOf(-1) }
    var yearSelected by remember { mutableStateOf(-1) }
    var daySelected by remember { mutableStateOf(-1) }
    var monthSelected by remember { mutableStateOf(-1) }

    var tempMonthSelected by remember { mutableStateOf(-1) }
    var tempDaySelected by remember { mutableStateOf(-1) }
    var tempYearSelected by remember { mutableStateOf(-1) }

    val topBox = stringResource(R.string.top_box)
    val needToDo = stringResource(R.string.what_to_do)
    val setDueDate = stringResource(R.string.set_due_date)
    val invalidTime = stringResource(R.string.invalid_time)

    var enabled by remember { mutableStateOf(false) }
    var textState by remember { mutableStateOf("") }
    var dateState by remember { mutableStateOf(setDueDate) }

    val errorTime = Toast.makeText(appContext, invalidTime, Toast.LENGTH_LONG)
    val datePicker = DatePickerDialog(appContext, R.style.MyDatePickerDialogTheme)

    val timePicker = TimePickerDialog(
        LocalContext.current, R.style.MyTimePickerDialogTheme,
        { _, hour, minute ->
            c = Calendar.getInstance()
            if (!isSelectionAfterToday(
                    tempDaySelected,
                    tempMonthSelected,
                    tempYearSelected
                ) && !isTimeInFuture(hour, minute, minTimeAhead)
            ) {
                errorTime.show()
            } else {
                yearSelected = tempYearSelected
                monthSelected = tempMonthSelected
                daySelected = tempDaySelected
                hourSelected = hour
                minuteSelected = minute
                dateState = formatDateString(
                    daySelected,
                    monthSelected,
                    yearSelected,
                    hourSelected,
                    minuteSelected,
                    setDueDate
                )
            }
        },
        c.get(Calendar.HOUR_OF_DAY),
        c.get(Calendar.MINUTE),
        false
    )

    datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
        tempYearSelected = year
        tempMonthSelected = month
        tempDaySelected = dayOfMonth

        c = Calendar.getInstance()
        if (hourSelected == -1 || minuteSelected == -1) {
            val timeAdded = addMinutesToTime(
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                minTimeAhead + 1
            )
            timePicker.updateTime(timeAdded.first, timeAdded.second)
        } else {
            timePicker.updateTime(hourSelected, minuteSelected)
        }
        timePicker.show()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(475.dp)
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
            Row(modifier = Modifier.padding(15.dp)) {
                Text(text = dateState,
                    modifier = Modifier
                        .border(2.dp, WhiteBackground, RoundedCornerShape(20.dp))
                        .clickable {
                            datePicker.datePicker.minDate =
                                System.currentTimeMillis() + minTimeAhead * 1000
                            if (yearSelected == -1 || monthSelected == -1 || daySelected == -1) {
                                datePicker.updateDate(
                                    c.get(Calendar.YEAR),
                                    c.get(Calendar.MONTH),
                                    c.get(Calendar.DAY_OF_MONTH)
                                )
                            } else {
                                datePicker.updateDate(yearSelected, monthSelected, daySelected)
                            }
                            datePicker.show()
                        }
                        .padding(10.dp),
                    color = WhiteTextColor,
                    textAlign = TextAlign.Center,
                    fontFamily = dmSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp)
            }
            Button(
                onClick = {
                    if (daySelected == -1 || !isSelectionAfterToday(
                            daySelected,
                            monthSelected,
                            yearSelected
                        ) && !isTimeInFuture(hourSelected, minuteSelected, minTimeAhead)
                    ) {
                        errorTime.show()
                    } else {
                        onTapSave(
                            textState,
                            "$daySelected/${monthSelected + 1}/$yearSelected $hourSelected:$minuteSelected"
                        )
                        keyboardController?.hide()
                        scope.launch {
                            scaffoldState.bottomSheetState.apply {
                                if (isCollapsed) expand() else collapse()
                            }
                        }
                        textState = ""
                        dateState = setDueDate
                        daySelected = -1
                        monthSelected = -1
                        yearSelected = -1
                        minuteSelected = -1
                        hourSelected = -1
                    }
                },
                enabled = enabled,
                shape = RoundedCornerShape(20),
                colors = ButtonDefaults.buttonColors(WhiteBackground),
                content = {
                    Text(
                        text = stringResource(id = R.string.save),
                        fontFamily = dmSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = if (enabled) BlackTextColor else LightGrey
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

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                modifier = Modifier.width(250.dp)
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
                    .border(2.dp, WhiteBackground, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = textState,
                    fontSize = 20.sp,
                    color = WhiteTextColor,
                    modifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 5.dp),
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
                    .border(2.dp, WhiteBackground, RoundedCornerShape(20.dp)),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.new_todoTitle),
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
            Spacer(modifier = Modifier.padding(horizontal = 15.dp))
            Box(
                modifier = Modifier
                    .shadow(2.dp, RoundedCornerShape(20))
                    .background(WhiteBackground)
                    .clickable { onClickCompleted() }
            ) {
                Text(
                    text = stringResource(id = R.string.completed),
                    Modifier
                        .padding(10.dp, 10.dp),
                    textAlign = TextAlign.Center,
                    color = ListDetailedViewBackGround,
                    fontSize = 18.sp,
                    fontFamily = dmSans,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.padding(27.dp))
    }
}

fun isSelectionAfterToday(
    selectDay: Int,
    selectMonth: Int,
    selectYear: Int,
    currentDay: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
    currentMonth: Int = Calendar.getInstance().get(Calendar.MONTH),
    currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)
): Boolean {
    return LocalDate.of(selectYear, selectMonth + 1, selectDay)
        .isAfter(LocalDate.of(currentYear, currentMonth + 1, currentDay))
}

fun isTimeInFuture(
    selectedHour: Int,
    selectedMinute: Int,
    minTimeAhead: Int,
    currentHour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
    currentMinute: Int = Calendar.getInstance().get(Calendar.MINUTE)
): Boolean {
    val newTime = addMinutesToTime(currentHour, currentMinute, minTimeAhead)
    return when {
        selectedHour > newTime.first -> true
        selectedHour == newTime.first -> selectedMinute > newTime.second
        else -> false
    }
}

//Use function to only add times that are less than an hour
fun addMinutesToTime(hour: Int, minute: Int, minutesToAdd: Int): Pair<Int, Int> {
    if (minute + minutesToAdd >= 60) {
        return if (hour + 1 == 24) {
            Pair(0, minute + minutesToAdd - 60)
        } else {
            Pair(hour + 1, minute + minutesToAdd - 60)
        }
    }
    return Pair(hour, minute + minutesToAdd)

}

fun formatDateString(
    day: Int,
    month: Int,
    year: Int,
    hour: Int,
    minute: Int,
    baseText: String
): String {
    if (day == -1 || month == -1 || year == -1 || hour == -1 || minute == -1) {
        return baseText
    }
    return "$day/${IntToMonth.convertIntMonthToString(month)}/$year at " +
            "${if (hour > 12) hour - 12 else if (hour == 0) "12" else hour}:" +
            "${if (minute < 10) "0$minute" else minute} ${if (hour >= 12) "PM" else "AM"}"
}