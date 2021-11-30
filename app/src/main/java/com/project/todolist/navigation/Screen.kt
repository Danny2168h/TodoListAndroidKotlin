package com.project.todolist.navigation

sealed class Screen(val route: String) {
    object MainScreen : Screen(route = "main_screen")
    object ListDetailedView : Screen(route = "listDetailedScreen_screen")
    object DetailedView : Screen(route = "detailedView_screen")
}