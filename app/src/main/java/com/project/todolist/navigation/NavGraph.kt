package com.project.todolist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.project.todolist.screens.entry.EntryDetailedScreen
import com.project.todolist.screens.main.MainScreen
import com.project.todolist.screens.todolist.ListDetailedScreen


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SetUpNavGraph() {
    val navigationControl = rememberNavController()

    NavHost(
        navController = navigationControl,
        startDestination = Screen.MainScreen.route //Change to change the start
    ) {
        composable(
            route = Screen.MainScreen.route
        ) {
            MainScreen(navController = navigationControl)
        }

        composable(
            route = Screen.ListDetailedView.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) { entry ->
            ListDetailedScreen(
                navController = navigationControl,
                listID = entry.arguments?.getLong("id") ?: 0
            )
        }
        composable(
            route = Screen.DetailedView.route + "/{todo_item}",
            arguments = listOf(
                navArgument("todo_item") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { entry ->
            EntryDetailedScreen(
                navController = navigationControl,
                title = entry.arguments?.getString("todo_item")
            )
        }
    }
}
