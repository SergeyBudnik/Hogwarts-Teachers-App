package com.bdev.hengschoolteacher.ui.page_fragments.lesson.info

import com.bdev.hengschoolteacher.data.school.group.Group
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType

data class LessonInfoPageFragmentData(
    val group: Group,
    val lesson: Lesson,
    val lessonStartTime: Long,
    val lessonStatus: LessonStatus?,
    val teacherName: String,
    val teacherSurname: String,
    val weekIndex: Int,
    val students: List<Pair<Pair<Student, StudentAttendanceType?>, Pair<Int, Int>>>
)