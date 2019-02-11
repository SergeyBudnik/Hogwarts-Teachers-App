package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.data.school.group.GroupType
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import java.lang.RuntimeException

@EBean
open class LessonsPaymentService {
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var studentsService: StudentsService

    fun getLessonOutcome(lessonId: Long) : Int {
        val group = lessonsService.getLessonGroup(lessonId) ?: throw RuntimeException()
        val lesson = lessonsService.getLesson(lessonId) ?: throw RuntimeException()
        val students = studentsService.getGroupStudents(group.id)

        val lessonLength = lesson.finishTime.order - lesson.startTime.order

        return when (group.type) {
            GroupType.INDIVIDUAL -> 550 * lessonLength
            GroupType.GROUP -> {
                if (students.size == 1) {
                    350 * lessonLength
                } else {
                    230 * lessonLength * students.size
                }
            }
        }
    }
}
