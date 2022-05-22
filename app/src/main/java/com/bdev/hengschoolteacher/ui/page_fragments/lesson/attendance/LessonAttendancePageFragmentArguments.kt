package com.bdev.hengschoolteacher.ui.page_fragments.lesson.attendance

import java.io.Serializable

data class LessonAttendancePageFragmentArguments(
    val lessonId: Long,
    val weekIndex: Int,
    val studentLogin: String
): Serializable