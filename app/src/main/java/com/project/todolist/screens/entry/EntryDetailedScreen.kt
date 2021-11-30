package com.project.todolist.screens.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun EntryDetailedScreen(navController: NavController, title: String?) {
    Column() {
        BackButtonDetailed(TapBack = { navController.popBackStack() })
        Text(text = title ?: "")
    }
}

@Composable
fun BackButtonDetailed(TapBack: () -> Unit) {
    Button(modifier = Modifier
        .background(color = Color.Blue)
        .size(20.dp), onClick = { TapBack() })
    {}
}