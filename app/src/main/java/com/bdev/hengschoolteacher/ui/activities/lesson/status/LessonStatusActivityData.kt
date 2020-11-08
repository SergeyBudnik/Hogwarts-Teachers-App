package com.bdev.hengschoolteacher.ui.activities.lesson.status

import java.io.Serializable

data class LessonStatusActivityData(
        val lessonId: Long,
        val weekIndex: Int
): Serializable