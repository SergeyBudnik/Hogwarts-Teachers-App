package com.bdev.hengschoolteacher.ui.activities.lesson.info

import java.io.Serializable

data class LessonInfoActivityData(
        val groupId: Long,
        val lessonId: Long,
        val weekIndex: Int
): Serializable