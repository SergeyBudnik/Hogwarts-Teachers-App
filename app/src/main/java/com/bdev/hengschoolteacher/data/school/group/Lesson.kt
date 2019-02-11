package com.bdev.hengschoolteacher.data.school.group

import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.Time
import java.io.Serializable

class Lesson(
        var id: Long,
        var teacherId: Long,
        var day: DayOfWeek,
        var startTime: Time,
        var finishTime: Time
): Serializable {
    @Suppress("UNUSED")
    constructor(): this(0L, 0L, DayOfWeek.MONDAY, Time.T_07_00, Time.T_07_00)
}