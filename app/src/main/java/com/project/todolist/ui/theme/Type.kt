package com.project.todolist.ui.theme


import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.project.todolist.R

val dmSans = FontFamily(
    Font(R.font.dmsans_medium),
    Font(R.font.dmsans_bold, weight = FontWeight.Bold),
    Font(R.font.dmsans_regular, weight = FontWeight.Light)
)

val inter = FontFamily(
    Font(R.font.inter)
)


// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = dmSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),

    body2 = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),

    h1 = TextStyle(
        fontFamily = dmSans,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),

    h2 = TextStyle(
        fontFamily = dmSans,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp
    ),


    )