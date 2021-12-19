package com.project.todolist.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.project.todolist.screens.completed.CompletedScreen
import com.project.todolist.screens.completed.CompletedScreenViewModel
import com.project.todolist.screens.entry.EntryDetailedScreen
import com.project.todolist.screens.entry.EntryDetailedScreenViewModel
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
            route = Screen.DetailedView.route + "/{todo_title}" + "/{todo_description}" + "/{todo_listID}" + "/{todo_itemID}",
            arguments = listOf(
                navArgument("todo_title") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("todo_description") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("todo_listID") {
                    type = NavType.LongType
                    defaultValue = 0
                },
                navArgument("todo_itemID") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { entry ->
            EntryDetailedScreen(
                navController = navigationControl,
                title = entry.arguments?.getString("todo_title"),
                description = entry.arguments?.getString("todo_description"),
                viewModel = EntryDetailedScreenViewModel(
                    navController = navigationControl,
                    listID = entry.arguments?.getLong("todo_listID"),
                    itemID = entry.arguments?.getString("todo_itemID")
                )
            )
        }
        composable(
            route = Screen.CompletedScreen.route
        ) {
            CompletedScreen(
                viewModel = CompletedScreenViewModel(navController = navigationControl),
            )
        }
    }
}
