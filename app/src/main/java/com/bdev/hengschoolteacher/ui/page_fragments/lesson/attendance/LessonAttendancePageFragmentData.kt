package com.bdev.hengschoolteacher.ui.page_fragments.lesson.attendance

import com.bdev.hengschoolteacher.data.school.group.Group
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType

data class LessonAttendancePageFragmentData(
    val lesson: Lesson,
    val group: Group,
    val student: Student,
    val attendance: StudentAttendanceType?,
    val progressAttendance: StudentAttendanceType?,
    val lessonStartTime: Long,
    val lessonFinishTime: Long,
    val lessonStudentsAmount: Int,
    val teacherName: String,
    val teacherSurname: String
)