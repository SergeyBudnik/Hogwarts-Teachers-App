package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.Time
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import com.bdev.hengschoolteacher.data.school.teacher.TeacherAction
import com.bdev.hengschoolteacher.data.school.teacher.TeacherActionType
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class TeacherActionsService {
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var lessonStatusService: LessonStatusService

    fun getTeacherActions(teacherId: Long): List<TeacherAction> {
        val teacherActions = ArrayList<TeacherAction>()

        val teacherLessons = lessonsService.getTeacherLessons(teacherId)

        for (dayOfWeek in DayOfWeek.values()) {
            val allDayTeacherLessons = (teacherLessons[dayOfWeek] ?: emptyList()).map { it.lesson }
            val passedDayTeacherLessons = getPassedTeacherDayLessons(allDayTeacherLessons)

            var previousLesson: Lesson? = null

            for (lesson in passedDayTeacherLessons) {
                if (teacherShouldDoTrip(previousLesson, lesson)) {
                    teacherActions.add(TeacherAction(
                            TeacherActionType.ROAD,
                            dayOfWeek,
                            Time.fromOrder(lesson.startTime.order - 1),
                            lesson.startTime
                    ))
                }

                teacherActions.add(TeacherAction(
                        TeacherActionType.LESSON,
                        dayOfWeek,
                        lesson.startTime,
                        lesson.finishTime
                ))

                previousLesson = lesson
            }
        }

        return teacherActions
    }

    private fun getPassedTeacherDayLessons(allTeacherDayLessons: List<Lesson>): List<Lesson> {
        return allTeacherDayLessons
                .asSequence()
                .filter {
                    val lessonStartTime = lessonsService.getLessonStartTime(it.id)
                    val lessonStatus = lessonStatusService.getLessonStatus(it.id, lessonStartTime)

                    lessonStatus?.type == LessonStatus.Type.FINISHED
                }
                .toList()
    }

    private fun teacherShouldDoTrip(previousLesson: Lesson?, currentLesson: Lesson): Boolean {
        if (previousLesson == null) {
            return true
        }

        return currentLesson.startTime.order - previousLesson.finishTime.order > 2
    }
}
