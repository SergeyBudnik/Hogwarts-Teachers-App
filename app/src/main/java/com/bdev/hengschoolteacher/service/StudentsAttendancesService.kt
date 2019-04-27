package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.dao.StudentsAttendancesDao
import com.bdev.hengschoolteacher.dao.StudentsAttendancesModel
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.utils.TimeUtils
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class StudentsAttendancesService {
    @Bean
    lateinit var studentsAttendancesDao: StudentsAttendancesDao
    @Bean
    lateinit var lessonsService: LessonsService

    fun getMonthlyAttendances(studentId: Long, month: Int): List<StudentAttendance> {
        val startTime = TimeUtils().getMonthStart(month)
        val finishTime = TimeUtils().getMonthFinish(month)

        return studentsAttendancesDao
                .readValue()
                .studentsAttendances
                .asSequence()
                .filter { it.studentId == studentId }
                .filter { it.startTime <= finishTime }
                .filter { it.startTime >= startTime }
                .sortedBy { it.startTime }
                .toList()
    }

    fun getAllAttendances(): List<StudentAttendance> {
        return studentsAttendancesDao.readValue().studentsAttendances
    }

    fun getAllStudentAttendances(studentId: Long): List<StudentAttendance> {
        return studentsAttendancesDao
                .readValue()
                .studentsAttendances
                .asSequence()
                .filter { it.studentId == studentId }
                .toList()
    }

    fun getAttendance(lessonId: Long, studentId: Long, weekIndex: Int): StudentAttendance.Type? {
        val attendance = studentsAttendancesDao
                .readValue()
                .studentsAttendances
                .asSequence()
                .filter { it.studentId == studentId }
                .filter { it.startTime == lessonsService.getLessonStartTime(lessonId, weekIndex) }
                .maxBy { it.id ?: -1 }

        return attendance?.type
    }

    fun setAttendances(attendances: List<StudentAttendance>) {
        studentsAttendancesDao.writeValue(StudentsAttendancesModel(
                attendances
        ))
    }

    fun addAttendance(attendance: StudentAttendance) {
        studentsAttendancesDao.writeValue(StudentsAttendancesModel(
                studentsAttendancesDao.readValue().studentsAttendances.union(listOf(attendance)).toList()
        ))
    }
}
