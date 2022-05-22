package com.bdev.hengschoolteacher.ui.page_fragments.lesson.status

import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatusType
import com.bdev.hengschoolteacher.data.school.staff.StaffMember

data class LessonStatusPageFragmentData(
    val lesson: Lesson,
    val lessonStartTime: Long,
    val teacher: StaffMember,
    val teacherName: String,
    val teacherSurname: String,
    val actualStatusType: LessonStatusType?,
    val progressStatusType: LessonStatusType?
)