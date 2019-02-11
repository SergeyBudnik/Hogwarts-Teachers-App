package com.bdev.hengschoolteacher.data.school.teacher

import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.Time

enum class TeacherActionType {
    ROAD, LESSON
}

class TeacherAction(
        val type: TeacherActionType,
        val dayOfWeek: DayOfWeek,
        val startTime: Time,
        val finishTime: Time
)