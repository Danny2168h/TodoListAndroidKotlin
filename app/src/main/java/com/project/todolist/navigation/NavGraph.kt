package com.project.todolist.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.project.todolist.screens.entry.EntryDetailedScreen
import com.project.todolist.screens.main.MainScreen
import com.project.todolist.screens.main.MainScreenViewModel
import com.project.todolist.screens.todolist.ListDetailedScreen
import com.project.todolist.screens.todolist.ListDetailedScreenViewModel


@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun SetUpNavGraph(navigationControl: NavHostController) {

    NavHost(
        navController = navigationControl,
        startDestination = Screen.MainScreen.route //Change to change the start
    ) {
        composable(
            route = Screen.MainScreen.route
        ) {
            MainScreen(MainScreenViewModel(navigationControl))
        }

        composable(
            route = Screen.ListDetailedView.route + "/{id}" + "/{title}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                },
                navArgument("title") {
                    type = NavType.StringType
                }
            )
        ) { entry ->
            ListDetailedScreen(
                ListDetailedScreenViewModel(
                    id = entry.arguments?.getLong("id") ?: 0,
                    navController = navigationControl
                ),
                todoTitle = entry.arguments?.getString("title") ?: "",
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
