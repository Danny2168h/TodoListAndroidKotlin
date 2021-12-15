package com.project.todolist.screens.completed

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.todolist.model.TodoItem

@Composable
fun CompletedScreen(viewModel: CompletedScreenViewModel) {
    val state by viewModel.state.collectAsState()

    CompletedScreenMain(
        completedItems = state.completedTodoItems,
        count = state.count
    )
}

@Composable
fun CompletedScreenMain(completedItems: List<TodoItem>, count: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(30.dp))
        Text(text = "$count")
        Spacer(modifier = Modifier.padding(30.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(35.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            items(completedItems) { completedItem ->
                Text(text = completedItem.title, fontSize = 15.sp, color = Color.Black)
                println(completedItem.title + "Description of completed item")
            }
        }
    }
}
