package com.bdev.hengschoolteacher.interactors

import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractorImpl
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonStatusStorageInteractorImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import java.util.*

interface LessonStateService {
    fun isLessonFilled(lesson: Lesson, weekIndex: Int): Boolean
    fun isLessonFinished(lessonId: Long, weekIndex: Int): Boolean
}

@EBean
open class LessonStateServiceImpl : LessonStateService {
    @Bean
    lateinit var lessonsAttendancesService: LessonsAttendancesServiceImpl
    @Bean
    lateinit var lessonStatusService: LessonStatusStorageInteractorImpl
    @Bean
    lateinit var lessonsService: LessonsInteractorImpl

    override fun isLessonFilled(lesson: Lesson, weekIndex: Int): Boolean {
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

    override fun isLessonFinished(lessonId: Long, weekIndex: Int): Boolean {
        return Date(lessonsService.getLessonFinishTime(lessonId, weekIndex)).before(Date())
    }
}
