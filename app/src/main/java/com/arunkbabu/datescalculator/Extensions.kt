package com.arunkbabu.datescalculator

fun Int.toMonthString() =
    when (this) {
        1 -> "January"
        2 -> "February"
        3 -> "March"
        4 -> "April"
        5 -> "May"
        6 -> "June"
        7 -> "July"
        8 -> "August"
        9 -> "September"
        10 -> "October"
        11 -> "November"
        12 -> "December"
        else -> "Invalid Month"
    }

fun Int.toWeekString() =
    when (this) {
        1 -> "Sunday"
        2 -> "Monday"
        3 -> "Tuesday"
        4 -> "Wednesday"
        5 -> "Thursday"
        6 -> "Friday"
        7 -> "Saturday"
        else -> "Invalid Week"
    }

fun Long.toCorrectDaysPassed(): String {
    return if (this >= 0) {
        this.inc().toString()
    } else {
        this.toString().trimStart('-')
    }
}