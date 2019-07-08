package com.bdev.hengschoolteacher.ui.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeFormatUtils {
    fun format(time: Long): String {
        return SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.US).format(Date(time))
    }

    fun formatOnlyDate(time: Long): String {
        return SimpleDateFormat("dd.MM.yyyy", Locale.US).format(Date(time))
    }

    fun formatOnlyTime(time: Long): String {
        return SimpleDateFormat("HH:mm", Locale.US).format(Date(time))
    }
}
