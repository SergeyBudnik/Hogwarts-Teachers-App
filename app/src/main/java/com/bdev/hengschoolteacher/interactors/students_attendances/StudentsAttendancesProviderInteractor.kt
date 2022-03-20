package com.bdev.hengschoolteacher.interactors.students_attendances

import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.utils.TimeUtils
import javax.inject.Inject

interface StudentsAttendancesProviderInteractor {
    fun getMonthlyAttendances(studentLogin: String, month: Int): List<StudentAttendance>
    fun getAllStudentAttendances(studentLogin: String): List<StudentAttendance>
    fun getAttendance(lessonId: Long, studentLogin: String, weekIndex: Int): StudentAttendanceType?
}

class StudentsAttendancesProviderInteractorImpl @Inject constructor(
    private val studentsAttendancesStorageInteractor: StudentsAttendancesStorageInteractor,
    private val lessonsService: LessonsInteractor
): StudentsAttendancesProviderInteractor {
    override fun getMonthlyAttendances(studentLogin: String, month: Int): List<StudentAttendance> {
        val startTime = TimeUtils().getMonthStart(month)
        val finishTime = TimeUtils().getMonthFinish(month)

        return studentsAttendancesStorageInteractor
                .getAll()
                .asSequence()
                .filter { it.studentLogin == studentLogin }
                .filter { it.startTime <= finishTime }
                .filter { it.startTime >= startTime }
                .sortedBy { it.startTime }
                .toList()
    }

    override fun getAllStudentAttendances(studentLogin: String): List<StudentAttendance> {
        return studentsAttendancesStorageInteractor
                .getAll()
                .asSequence()
                .filter { it.studentLogin == studentLogin }
                .toList()
    }

    override fun getAttendance(lessonId: Long, studentLogin: String, weekIndex: Int): StudentAttendanceType? {
        val attendance = studentsAttendancesStorageInteractor
                .getAll()
                .asSequence()
                .filter { it.studentLogin == studentLogin }
                .filter { it.startTime == lessonsService.getLessonStartTime(lessonId, weekIndex) }
                .find { true }

        return attendance?.type
    }
}
