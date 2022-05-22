package com.bdev.hengschoolteacher.utils

import java.util.*

object CalendarUtils {
    fun getInstance(): Calendar = Calendar.getInstance().also { calendar ->
        calendar.firstDayOfWeek = Calendar.MONDAY
    }
}