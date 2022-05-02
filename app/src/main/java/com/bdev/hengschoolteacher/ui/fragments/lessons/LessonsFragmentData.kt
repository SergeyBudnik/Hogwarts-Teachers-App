package com.bdev.hengschoolteacher.ui.fragments.lessons

import com.bdev.hengschoolteacher.ui.views.app.lesson.LessonRowViewData

data class LessonsFragmentData(
    val lessons: List<LessonRowViewData>,
    val weekIndex: Int,
    val filterEnabled: Boolean
)