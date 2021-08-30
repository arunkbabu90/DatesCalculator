package com.arunkbabu.datescalculator

data class TemporalCapsule(
    val day1: Int = -1,
    val day2: Int = -1,
    val dayOfWeek1: Int = -1,
    val dayOfWeek2: Int = -1,
    val weekOfMonth1: Int = -1,
    val weekOfMonth2: Int = -1,
    val month1: Int = -1,
    val month2: Int = -1,
    val year1: Int = -1,
    val year2: Int = -1,
    val daysPassed: Long = -1,
    val yearsPassed: Long = -1,
    val monthsPassed: Long = -1,
    val weeksPassed: Long = -1,
    val hoursPassed: Long = -1,
    val minutesPassed: Long = -1,
    val secondsPassed: Long = -1
)
