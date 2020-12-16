package com.bdev.hengschoolteacher.utils

import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.Time
import java.util.*

class TimeUtils {
    fun getCurrentMonth(): Int {
        return Calendar.getInstance().get(Calendar.MONTH)
    }

    fun getMonthStart(month: Int): Long {
        val calendar = Calendar.getInstance()

        with (calendar) {
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE,      0)
            set(Calendar.SECOND,      0)
            set(Calendar.MILLISECOND, 0)
        }

        return calendar.timeInMillis
    }

    fun getMonthFinish(month: Int): Long {
        val calendar = Calendar.getInstance()

        with (calendar) {
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE,      59)
            set(Calendar.SECOND,      59)
            set(Calendar.MILLISECOND, 0)
        }

        return calendar.timeInMillis
    }

    fun getWeekStart(weekFromCurrent: Int): Long {
        val calendar = Calendar.getInstance()

        val currentWeekYear = calendar.get(Calendar.WEEK_OF_YEAR)

        with (calendar) {
            set(Calendar.WEEK_OF_YEAR, currentWeekYear + weekFromCurrent)
            set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE,      0)
            set(Calendar.SECOND,      0)
            set(Calendar.MILLISECOND, 0)
        }

        return calendar.timeInMillis
    }

    fun getWeekFinish(weekFromCurrent: Int): Long {
        val calendar = Calendar.getInstance()

        val currentWeekYear = calendar.get(Calendar.WEEK_OF_YEAR)

        with (calendar) {
            set(Calendar.WEEK_OF_YEAR, currentWeekYear + weekFromCurrent)
            set(Calendar.DAY_OF_WEEK, getLastDayOfWeek(calendar))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE,      59)
            set(Calendar.SECOND,      59)
            set(Calendar.MILLISECOND, 0)
        }

        return calendar.timeInMillis
    }

    fun getDayOfWeek(calendar: Calendar) : DayOfWeek {
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> DayOfWeek.MONDAY
            Calendar.TUESDAY -> DayOfWeek.TUESDAY
            Calendar.WEDNESDAY -> DayOfWeek.WEDNESDAY
            Calendar.THURSDAY -> DayOfWeek.THURSDAY
            Calendar.FRIDAY -> DayOfWeek.FRIDAY
            Calendar.SATURDAY -> DayOfWeek.SATURDAY
            Calendar.SUNDAY -> DayOfWeek.SUNDAY
            else -> throw RuntimeException()
        }
    }

    fun getCalendayDayOfWeek(dayOfWeek: DayOfWeek) : Int {
        return when (dayOfWeek) {
            DayOfWeek.MONDAY -> Calendar.MONDAY
            DayOfWeek.TUESDAY -> Calendar.TUESDAY
            DayOfWeek.WEDNESDAY -> Calendar.WEDNESDAY
            DayOfWeek.THURSDAY -> Calendar.THURSDAY
            DayOfWeek.FRIDAY -> Calendar.FRIDAY
            DayOfWeek.SATURDAY -> Calendar.SATURDAY
            DayOfWeek.SUNDAY -> Calendar.SUNDAY
        }
    }

    fun getCalendarHour(time: Time) : Int {
        return when (time) {
            Time.T_07_00, Time.T_07_30 -> 7
            Time.T_08_00, Time.T_08_30 -> 8
            Time.T_09_00, Time.T_09_30 -> 9
            Time.T_10_00, Time.T_10_30 -> 10
            Time.T_11_00, Time.T_11_30 -> 11
            Time.T_12_00, Time.T_12_30 -> 12
            Time.T_13_00, Time.T_13_30 -> 13
            Time.T_14_00, Time.T_14_30 -> 14
            Time.T_15_00, Time.T_15_30 -> 15
            Time.T_16_00, Time.T_16_30 -> 16
            Time.T_17_00, Time.T_17_30 -> 17
            Time.T_18_00, Time.T_18_30 -> 18
            Time.T_19_00, Time.T_19_30 -> 19
            Time.T_20_00, Time.T_20_30 -> 20
            Time.T_21_00, Time.T_21_30 -> 21
        }
    }

    fun getCalendarMinute(time: Time) : Int {
        return when (time) {
            Time.T_07_00, Time.T_08_00, Time.T_09_00, Time.T_10_00, Time.T_11_00,
            Time.T_12_00, Time.T_13_00, Time.T_14_00, Time.T_15_00, Time.T_16_00,
            Time.T_17_00, Time.T_18_00, Time.T_19_00, Time.T_20_00, Time.T_21_00 -> 0

            Time.T_07_30, Time.T_08_30, Time.T_09_30, Time.T_10_30, Time.T_11_30,
            Time.T_12_30, Time.T_13_30, Time.T_14_30, Time.T_15_30, Time.T_16_30,
            Time.T_17_30, Time.T_18_30, Time.T_19_30, Time.T_20_30, Time.T_21_30 -> 30
        }
    }

    private fun getLastDayOfWeek(calendar: Calendar): Int {
        return when (calendar.firstDayOfWeek) {
            Calendar.MONDAY -> Calendar.SUNDAY
            Calendar.SUNDAY -> Calendar.SATURDAY
            else -> Calendar.SUNDAY
        }
    }
}
