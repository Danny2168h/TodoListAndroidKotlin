package com.project.todolist.model

class IntToMonth {

    companion object {
        fun convertIntMonthToString(month: Int): String {
            return listOf(
                "Jan",
                "Feb",
                "Mar",
                "Apr",
                "May",
                "Jun",
                "Jul",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec"
            )[month]
        }
    }
}