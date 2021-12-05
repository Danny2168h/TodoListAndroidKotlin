package com.project.todolist.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.project.todolist.navigation.Screen

@Composable
fun MainScreen(navController: NavController) {

    val viewModel = viewModel(MainScreenViewModel::class.java)
    val state by viewModel.state.collectAsState()

    Column {
        Spacer(modifier = Modifier.padding(80.dp))
        Button(modifier = Modifier
            .background(color = Color.Blue)
            .size(30.dp), onClick = { viewModel.onButtonTap() }) {
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
        ) {
            items(state.todoLists) { todoList ->
                Box(modifier = Modifier.clickable { navController.navigate(Screen.ListDetailedView.route + "/${todoList.id}") }) {
                    Text(text = todoList.title, color = Color.Black, fontSize = 20.sp)
                }
            }
        }
    }
}

@Composable
fun MainScreenMain() {

}




