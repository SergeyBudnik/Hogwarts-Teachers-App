package com.bdev.hengschoolteacher.data.school

import com.bdev.hengschoolteacher.R
import java.util.*

enum class DayOfWeek constructor(
    val index: Int,
    val shortNameId: Int
) {
    MONDAY(1, R.string.monday_short),
    TUESDAY(2, R.string.tuesday_short),
    WEDNESDAY(3, R.string.wednesday_short),
    THURSDAY(4, R.string.thursday_short),
    FRIDAY(5, R.string.friday_short),
    SATURDAY(6, R.string.saturday_short),
    SUNDAY(7, R.string.sunday_short);
}
