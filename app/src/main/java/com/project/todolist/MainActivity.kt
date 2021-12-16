package com.project.todolist

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.rememberNavController
import com.project.todolist.navigation.SetUpNavGraph

class MainActivity : ComponentActivity() {

    init {
        instance = this
    }

    companion object {
        private var instance: MainActivity? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        Graph.provide(this)
        setContent {
            val navigationControl = rememberNavController()
            SetUpNavGraph(navigationControl)
        }
    }
}




