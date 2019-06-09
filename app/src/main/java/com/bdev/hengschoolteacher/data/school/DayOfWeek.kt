package com.bdev.hengschoolteacher.data.school

import com.bdev.hengschoolteacher.R
import java.util.*

enum class DayOfWeek constructor(
    private val mondayFirstIndex: Int,
    private val sundayFirstIndex: Int,
    val shortNameId: Int
) {
    MONDAY(1, 2, R.string.monday_short),
    TUESDAY(2, 3, R.string.tuesday_short),
    WEDNESDAY(3, 4, R.string.wednesday_short),
    THURSDAY(4, 5, R.string.thursday_short),
    FRIDAY(5, 6, R.string.friday_short),
    SATURDAY(6, 7, R.string.saturday_short),
    SUNDAY(7, 1, R.string.sunday_short);

    fun compare(dayOfWeek: DayOfWeek, calendar: Calendar): Int {
        return when (calendar.firstDayOfWeek) {
            Calendar.MONDAY -> mondayFirstIndex.compareTo(dayOfWeek.mondayFirstIndex)
            Calendar.SUNDAY -> sundayFirstIndex.compareTo(dayOfWeek.sundayFirstIndex)
            else -> mondayFirstIndex.compareTo(dayOfWeek.mondayFirstIndex)
        }
    }
}
