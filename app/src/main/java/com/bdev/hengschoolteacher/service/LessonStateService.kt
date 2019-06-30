package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.data.school.group.Lesson
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class LessonStateService {
    @Bean
    lateinit var lessonsAttendancesService: LessonsAttendancesService
    @Bean
    lateinit var lessonStatusService: LessonStatusService
    @Bean
    lateinit var lessonsService: LessonsService

    fun isLessonFilled(lesson: Lesson, weekIndex: Int): Boolean {
        val attendanceFilled = lessonsAttendancesService.isLessonAttendanceFilled(
                lessonId = lesson.id,
                weekIndex = weekIndex
        )

        val statusFilled = lessonStatusService.getLessonStatus(
                lessonId = lesson.id,
                lessonTime = lessonsService.getLessonStartTime(lesson.id, weekIndex)
        ) != null

        return attendanceFilled && statusFilled
    }
}
