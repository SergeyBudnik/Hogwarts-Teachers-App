package com.bdev.hengschoolteacher.interactors.students_attendances

import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractorImpl
import com.bdev.hengschoolteacher.utils.TimeUtils
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface StudentsAttendancesProviderService {
    fun getMonthlyAttendances(studentLogin: String, month: Int): List<StudentAttendance>
    fun getAllStudentAttendances(studentLogin: String): List<StudentAttendance>
    fun getAttendance(lessonId: Long, studentLogin: String, weekIndex: Int): StudentAttendanceType?
}

@EBean
open class StudentsAttendancesProviderServiceImpl : StudentsAttendancesProviderService {
    @Bean
    lateinit var studentsAttendancesStorageService: StudentsAttendancesStorageServiceImpl
    @Bean
    lateinit var lessonsService: LessonsInteractorImpl

    override fun getMonthlyAttendances(studentLogin: String, month: Int): List<StudentAttendance> {
        val startTime = TimeUtils().getMonthStart(month)
        val finishTime = TimeUtils().getMonthFinish(month)

        return studentsAttendancesStorageService
                .getAll()
                .asSequence()
                .filter { it.studentLogin == studentLogin }
                .filter { it.startTime <= finishTime }
                .filter { it.startTime >= startTime }
                .sortedBy { it.startTime }
                .toList()
    }

    override fun getAllStudentAttendances(studentLogin: String): List<StudentAttendance> {
        return studentsAttendancesStorageService
                .getAll()
                .asSequence()
                .filter { it.studentLogin == studentLogin }
                .toList()
    }

    override fun getAttendance(lessonId: Long, studentLogin: String, weekIndex: Int): StudentAttendanceType? {
        val attendance = studentsAttendancesStorageService
                .getAll()
                .asSequence()
                .filter { it.studentLogin == studentLogin }
                .filter { it.startTime == lessonsService.getLessonStartTime(lessonId, weekIndex) }
                .find { true }

        return attendance?.type
    }
}
