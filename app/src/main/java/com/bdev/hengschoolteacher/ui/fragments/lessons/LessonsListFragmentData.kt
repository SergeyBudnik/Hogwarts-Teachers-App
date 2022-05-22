package com.bdev.hengschoolteacher.ui.fragments.lessons

import com.bdev.hengschoolteacher.data.school.group.Group
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType

data class LessonsFragmentData(
    val lessons: List<LessonsFragmentItemData>,
    val weekIndex: Int,
    val filterEnabled: Boolean
)

data class LessonsFragmentItemData(
    val staffMember: StaffMember?,
    val group: Group,
    val lesson: Lesson,
    val lessonStatus: LessonStatus?,
    val isLessonFinished: Boolean,
    val isLessonAttendanceFilled: Boolean,
    val studentsToAttendanceType: List<Pair<Student, StudentAttendanceType?>>,
    val weekIndex: Int,
    val showTeacher: Boolean
)