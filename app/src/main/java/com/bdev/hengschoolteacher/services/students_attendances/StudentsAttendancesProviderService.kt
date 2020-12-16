package com.bdev.hengschoolteacher.services.students_attendances

import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType
import com.bdev.hengschoolteacher.services.lessons.LessonsService
import com.bdev.hengschoolteacher.utils.TimeUtils
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class StudentsAttendancesProviderService {
    @Bean
    lateinit var studentsAttendancesStorageService: StudentsAttendancesStorageService
    @Bean
    lateinit var lessonsService: LessonsService

    fun getMonthlyAttendances(studentLogin: String, month: Int): List<StudentAttendance> {
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

    fun getAllStudentAttendances(studentLogin: String): List<StudentAttendance> {
        return studentsAttendancesStorageService
                .getAll()
                .asSequence()
                .filter { it.studentLogin == studentLogin }
                .toList()
    }

    fun getAttendance(lessonId: Long, studentLogin: String, weekIndex: Int): StudentAttendanceType? {
        val attendance = studentsAttendancesStorageService
                .getAll()
                .asSequence()
                .filter { it.studentLogin == studentLogin }
                .filter { it.startTime == lessonsService.getLessonStartTime(lessonId, weekIndex) }
                .find { true }

        return attendance?.type
    }
}
