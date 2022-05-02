package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.lessons

import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.data.school.staff.StaffMember

data class MonitoringLessonsPageFragmentData(
    val filterEnabled: Boolean,
    val calendarEnabled: Boolean,

    val weekIndex: Int,

    val lessons: List<GroupAndLesson>
)