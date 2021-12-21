package com.project.todolist.model

class IntToMonth {

    companion object {
        fun convertIntMonthToString(month: Int): String {
            when (month) {
                0 -> {
                    return "Jan"
                }
                1 -> {
                    return "Feb"
                }
                2 -> {
                    return "March"
                }
                3 -> {
                    return "Apr"
                }
                4 -> {
                    return "May"
                }
                5 -> {
                    return "Jun"
                }
                6 -> {
                    return "July"
                }
                7 -> {
                    return "Aug"
                }
                8 -> {
                    return "Sep"
                }
                9 -> {
                    return "Oct"
                }
                10 -> {
                    return "Nov"
                }
                11 -> {
                    return "Dec"
                }
                else -> return "Invalid Input"
            }
        }
    }
}