package com.project.todolist.screens.completed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.todolist.R
import com.project.todolist.model.TodoItem
import com.project.todolist.ui.theme.*

@Composable
fun CompletedScreen(viewModel: CompletedScreenViewModel) {
    val state by viewModel.state.collectAsState()

    CompletedScreenMain(
        completedItems = state.completedTodoItems,
        count = state.count,
        tapBack = { viewModel.tapBack() },
        tapCompletedItem = { viewModel.tapCompletedItem() }
    )
}

@Composable
fun CompletedScreenMain(
    completedItems: List<TodoItem>,
    count: Int,
    tapBack: () -> Unit,
    tapCompletedItem: () -> Unit
) {
    TodoListTheme() {
        Box(modifier = Modifier
            .background(WhiteTextColor)
            .fillMaxWidth()
            .fillMaxHeight()) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                TitleArea(
                    tapBack = { tapBack() },
                    count = count
                )
                completedItemsList(
                    tapCompletedItem = { tapCompletedItem() },
                    completedItems = completedItems
                )
            }
        }
    }
}

@Composable
fun completedItemsList(tapCompletedItem: () -> Unit, completedItems: List<TodoItem>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        )
        {
            items(completedItems) { entry ->
                CompletedItemUI(
                    entry,
                    tapCompletedItem = { tapCompletedItem() },
                )
            }
        }
    }
}

@Composable
fun CompletedItemUI(entry: TodoItem, tapCompletedItem: () -> Unit) {
    val arrowRight = stringResource(R.string.arrow_right)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp)
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(10))
            .sizeIn(minHeight = 100.dp)
            .background(LightGreyBackground)
            .clickable { tapCompletedItem() }
    ) {
        Text(
            text = entry.title,
            fontFamily = montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = BlackTextColor,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .width(290.dp)
        )
        Icon(
            Icons.Rounded.ArrowForwardIos,
            "",
            modifier = Modifier
                .semantics { testTag = arrowRight }
                .size(18.dp),
        )
    }
}

@Composable
fun TitleArea(
    tapBack: () -> Unit,
    count: Int
) {
    val goBack = stringResource(R.string.go_back)
    val delete = stringResource(R.string.delete)
    val checkSave = stringResource(R.string.check_save)

    var enabledDeleteLists by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
            .fillMaxWidth()
            .background(DarkerBlue)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.padding(0.dp, 20.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    Icons.Rounded.ArrowBackIosNew,
                    contentDescription = null,
                    tint = WhiteBackground,
                    modifier = Modifier
                        .size(22.dp)
                        .semantics { testTag = goBack }
                        .clickable { tapBack() })
                Text(
                    text = stringResource(id = R.string.todo_title),
                    color = WhiteTextColor,
                    fontSize = 25.sp,
                    fontFamily = montserrat,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(292.dp)
                )
                if (enabledDeleteLists) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null,
                        tint = WhiteBackground,
                        modifier = Modifier
                            .size(22.dp)
                            .semantics { testTag = checkSave }
                            .clickable {
                                enabledDeleteLists = false
                            })
                } else {
                    Icon(
                        Icons.Rounded.Delete,
                        contentDescription = null,
                        tint = WhiteBackground,
                        modifier = Modifier
                            .size(22.dp)
                            .semantics { testTag = delete }
                            .clickable {
                                enabledDeleteLists = true
                            })
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.completed_title),
                    color = WhiteTextColor,
                    fontSize = 30.sp,
                    fontFamily = montserrat,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = "$count " + stringResource(id = R.string.tasks_completed),
                    color = WhiteTextColor,
                    fontSize = 18.sp,
                    fontFamily = dmSans
                )

            }
        }
    }
}
