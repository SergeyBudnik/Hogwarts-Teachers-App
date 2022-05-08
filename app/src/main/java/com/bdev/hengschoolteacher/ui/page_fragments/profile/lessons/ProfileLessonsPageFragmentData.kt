package com.bdev.hengschoolteacher.ui.page_fragments.profile.lessons

import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson

data class ProfileLessonsPageFragmentData(
    val lessons: List<GroupAndLesson>,
    val weekIndex: Int,
    val filterEnabled: Boolean,
    val calendarEnabled: Boolean
)