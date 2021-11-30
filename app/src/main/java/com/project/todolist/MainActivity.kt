package com.project.todolist

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.todolist.navigation.SetUpNavGraph
import com.project.todolist.testData.ScreenUiData


class MainActivity : ComponentActivity() {

    var state = ScreenUiData()
    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        Graph.provide(this)
        setContent {
            navController = rememberNavController()
            SetUpNavGraph(navigationControl = navController, state = state)
        }
    }
}




