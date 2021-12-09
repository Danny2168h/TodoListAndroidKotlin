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

val openSans = FontFamily(
    Font(R.font.opensans_bold)
)

val montserrat = FontFamily(
    Font(R.font.mont_light, weight = FontWeight.Light),
    Font(R.font.mont_medium, weight = FontWeight.Normal),
    Font(R.font.mont_semibold, weight = FontWeight.SemiBold),
    Font(R.font.mont_extrabold, weight = FontWeight.ExtraBold)
)

val cormorant = FontFamily(
    Font(R.font.cormorant_bold, weight = FontWeight.Bold),
    Font(R.font.cormorant_semibold, weight = FontWeight.SemiBold)
)

val josefinsans = FontFamily(
    Font(R.font.josefinsans_bold, weight = FontWeight.Bold),
    Font(R.font.josefinsans_semibold, weight = FontWeight.SemiBold)
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