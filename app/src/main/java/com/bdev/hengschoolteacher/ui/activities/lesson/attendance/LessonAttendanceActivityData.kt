package com.bdev.hengschoolteacher.ui.activities.lesson.attendance

import java.io.Serializable

data class LessonAttendanceActivityData(
        val lessonId: Long,
        val studentLogin: String,
        val weekIndex: Int
): Serializable