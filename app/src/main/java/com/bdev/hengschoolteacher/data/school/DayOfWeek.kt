package com.bdev.hengschoolteacher.data.school

import com.bdev.hengschoolteacher.R
enum class DayOfWeek constructor(
    val index: Int,
    val shortNameId: Int
) {
    MONDAY(2, R.string.monday_short),
    TUESDAY(3, R.string.tuesday_short),
    WEDNESDAY(4, R.string.wednesday_short),
    THURSDAY(5, R.string.thursday_short),
    FRIDAY(6, R.string.friday_short),
    SATURDAY(7, R.string.saturday_short),
    SUNDAY(1, R.string.sunday_short);

    companion object {
        fun fromIndex(index: Int): DayOfWeek? {
            return DayOfWeek.values().first { it.index == index }
        }
    }
}
