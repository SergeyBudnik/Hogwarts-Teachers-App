package com.bdev.hengschoolteacher.ui.page_fragments.lesson.status

import java.io.Serializable

data class LessonStatusPageFragmentArguments(
    val lessonId: Long,
    val weekIndex: Int
): Serializable