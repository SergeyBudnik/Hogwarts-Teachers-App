package com.bdev.hengschoolteacher.ui.page_fragments.lesson.info.data

import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType

data class LessonInfoStudent(
    val name: String,
    val login: String,
    val attendanceType: StudentAttendanceType?,
    val currentDebt: Int,
    val expectedDebt: Int
)