package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.service.student_attendance.StudentsAttendancesProviderService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class LessonsAttendancesService {
    @Bean
    lateinit var studentsService: StudentsService
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var studentsAttendancesProviderService: StudentsAttendancesProviderService

    fun isLessonAttendanceFilled(lessonId: Long, weekIndex: Int): Boolean {
        return lessonsService
                .getLessonStudents(lessonId = lessonId, weekIndex = weekIndex)
                .map { studentsAttendancesProviderService.getAttendance(lessonId, it.login, weekIndex) }
                .filter { it == null }
                .none()
    }
}
