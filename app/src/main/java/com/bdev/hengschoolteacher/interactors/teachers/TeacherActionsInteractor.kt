package com.bdev.hengschoolteacher.interactors.teachers

import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.Time
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatusType
import com.bdev.hengschoolteacher.data.school.teacher.TeacherAction
import com.bdev.hengschoolteacher.data.school.teacher.TeacherActionType
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonsStatusStorageInteractor
import javax.inject.Inject

interface TeacherActionsInteractor {
    fun getTeacherActions(teacherLogin: String, weekIndex: Int): List<TeacherAction>
}

class TeacherActionsInteractorImpl @Inject constructor(
    private val lessonsService: LessonsInteractor,
    private val lessonsStatusService: LessonsStatusStorageInteractor
): TeacherActionsInteractor {
    override fun getTeacherActions(teacherLogin: String, weekIndex: Int): List<TeacherAction> {
        val teacherActions = ArrayList<TeacherAction>()

        val teacherLessons = lessonsService.getTeacherLessons(teacherLogin, weekIndex)

        for (dayOfWeek in DayOfWeek.values()) {
            val allDayTeacherLessons = teacherLessons.map { it.lesson }.filter { it.day == dayOfWeek }
            val passedDayTeacherLessons = getPassedTeacherDayLessons(allDayTeacherLessons, weekIndex)

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
                        if (lesson.isOnline) {
                            TeacherActionType.ONLINE_LESSON
                        } else {
                            TeacherActionType.LESSON
                        },
                        dayOfWeek,
                        lesson.startTime,
                        lesson.finishTime
                ))

                previousLesson = lesson
            }
        }

        return teacherActions
    }

    private fun getPassedTeacherDayLessons(allTeacherDayLessons: List<Lesson>, weekIndex: Int): List<Lesson> {
        return allTeacherDayLessons
                .asSequence()
                .filter {
                    val lessonStartTime = lessonsService.getLessonStartTime(it.id, weekIndex)
                    val lessonStatus = lessonsStatusService.getLessonStatus(it.id, lessonStartTime)

                    lessonStatus?.type == LessonStatusType.FINISHED
                }
                .sortedBy { it.startTime }
                .sortedBy { it.day }
                .toList()
    }

    private fun teacherShouldDoTrip(previousLesson: Lesson?, currentLesson: Lesson): Boolean {
        return if (currentLesson.isOnline) {
            false
        } else if (previousLesson == null || previousLesson.isOnline) {
            true
        } else {
            currentLesson.startTime.order - previousLesson.finishTime.order > 2
        }
    }
}
