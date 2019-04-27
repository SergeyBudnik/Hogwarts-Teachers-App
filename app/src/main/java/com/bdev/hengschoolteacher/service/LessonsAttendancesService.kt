package com.bdev.hengschoolteacher.service

import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class LessonsAttendancesService {
    @Bean
    lateinit var studentsService: StudentsService
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var studentsAttendancesService: StudentsAttendancesService

    fun isLessonAttendanceFilled(lessonId: Long, weekIndex: Int): Boolean {
        return lessonsService
                .getLessonStudents(lessonId = lessonId, weekIndex = weekIndex)
                .map { studentsAttendancesService.getAttendance(lessonId, it.id, weekIndex) }
                .filter { it == null }
                .none()
    }
}
