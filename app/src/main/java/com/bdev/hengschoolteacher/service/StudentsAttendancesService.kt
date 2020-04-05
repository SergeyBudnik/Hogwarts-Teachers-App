package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.dao.StudentsAttendancesDao
import com.bdev.hengschoolteacher.dao.StudentsAttendancesModel
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType
import com.bdev.hengschoolteacher.utils.TimeUtils
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class StudentsAttendancesService {
    @Bean
    lateinit var studentsAttendancesDao: StudentsAttendancesDao
    @Bean
    lateinit var lessonsService: LessonsService

    fun getMonthlyAttendances(studentLogin: String, month: Int): List<StudentAttendance> {
        val startTime = TimeUtils().getMonthStart(month)
        val finishTime = TimeUtils().getMonthFinish(month)

        return studentsAttendancesDao
                .readValue()
                .studentsAttendances
                .asSequence()
                .filter { it.studentLogin == studentLogin }
                .filter { it.startTime <= finishTime }
                .filter { it.startTime >= startTime }
                .sortedBy { it.startTime }
                .toList()
    }

    fun getAllStudentAttendances(studentLogin: String): List<StudentAttendance> {
        return studentsAttendancesDao
                .readValue()
                .studentsAttendances
                .asSequence()
                .filter { it.studentLogin == studentLogin }
                .toList()
    }

    fun getAttendance(lessonId: Long, studentLogin: String, weekIndex: Int): StudentAttendanceType? {
        val attendance = studentsAttendancesDao
                .readValue()
                .studentsAttendances
                .asSequence()
                .filter { it.studentLogin == studentLogin }
                .filter { it.startTime == lessonsService.getLessonStartTime(lessonId, weekIndex) }
                .find { true }

        return attendance?.type
    }

    fun setAttendances(attendances: List<StudentAttendance>) {
        studentsAttendancesDao.writeValue(StudentsAttendancesModel(
                attendances
        ))
    }

    fun addAttendance(attendance: StudentAttendance) {
        val res = studentsAttendancesDao
                .readValue()
                .studentsAttendances
                .filter {
                    val studentLoginMatches = it.studentLogin == attendance.studentLogin
                    val startTimeMatches = it.startTime == attendance.startTime

                    return@filter !studentLoginMatches || !startTimeMatches
                }
                .union(listOf(attendance))
                .toList()

        studentsAttendancesDao.writeValue(StudentsAttendancesModel(res))
    }
}
