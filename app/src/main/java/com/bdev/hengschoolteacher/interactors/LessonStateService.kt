package com.bdev.hengschoolteacher.interactors

import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonsStatusStorageInteractor
import java.util.*
import javax.inject.Inject

interface LessonStateService {
    fun isLessonFilled(lesson: Lesson, weekIndex: Int): Boolean
    fun isLessonFinished(lessonId: Long, weekIndex: Int): Boolean
}

class LessonStateServiceImpl @Inject constructor(
    private val lessonsAttendancesService: LessonsAttendancesService,
    private val lessonsStatusService: LessonsStatusStorageInteractor,
    private val lessonsService: LessonsInteractor
): LessonStateService {
    override fun isLessonFilled(lesson: Lesson, weekIndex: Int): Boolean {
        val attendanceFilled = lessonsAttendancesService.isLessonAttendanceFilled(
                lessonId = lesson.id,
                weekIndex = weekIndex
        )

        val statusFilled = lessonsStatusService.getLessonStatus(
                lessonId = lesson.id,
                lessonTime = lessonsService.getLessonStartTime(lesson.id, weekIndex)
        ) != null

        return attendanceFilled && statusFilled
    }

    override fun isLessonFinished(lessonId: Long, weekIndex: Int): Boolean {
        return Date(lessonsService.getLessonFinishTime(lessonId, weekIndex)).before(Date())
    }
}
