package com.bdev.hengschoolteacher.ui.page_fragments.profile.lessons

import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.ui.views.app.lesson.LessonRowViewData
import com.bdev.hengschoolteacher.ui.views.app.lessons.LessonsViewData

data class ProfileLessonsPageFragmentData(
    val me: StaffMember,
    val lessons: LessonsViewData,
    val noLessons: Boolean,
    val weekIndex: Int,
    val filterEnabled: Boolean,
    val calendarEnabled: Boolean
)