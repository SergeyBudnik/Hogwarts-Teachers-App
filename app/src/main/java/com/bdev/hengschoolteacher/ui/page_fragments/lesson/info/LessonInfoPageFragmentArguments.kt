package com.bdev.hengschoolteacher.ui.page_fragments.lesson.info

import java.io.Serializable

data class LessonInfoPageFragmentArguments(
    val groupId: Long,
    val lessonId: Long,
    val weekIndex: Int
): Serializable