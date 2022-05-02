package com.bdev.hengschoolteacher.ui.page_fragments.profile.lessons

import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.ui.views.app.lesson.LessonRowViewData
import com.bdev.hengschoolteacher.ui.views.app.lessons.LessonsViewData

data class ProfileLessonsPageFragmentData(
    val lessons: List<GroupAndLesson>,
    val weekIndex: Int,
    val filterEnabled: Boolean,
    val calendarEnabled: Boolean
)