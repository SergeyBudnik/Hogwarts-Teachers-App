package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.data.school.group.Lesson
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import java.util.*

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

    fun isLessonFinished(lessonId: Long, weekIndex: Int): Boolean {
        return Date(lessonsService.getLessonFinishTime(lessonId, weekIndex)).before(Date())
    }
}
