package com.bdev.hengschoolteacher.interactors.alerts.monitoring

import com.bdev.hengschoolteacher.interactors.LessonStateService
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import javax.inject.Inject

interface AlertsMonitoringLessonsInteractor {
    fun haveAlerts(): Boolean
}

class AlertsMonitoringLessonsInteractorImpl @Inject constructor(
    private val lessonsService: LessonsInteractor,
    private val lessonStateService: LessonStateService
): AlertsMonitoringLessonsInteractor {
    private val monitoringWeeksAmount = 6

    override fun haveAlerts(): Boolean {
        var haveAlerts = false

        for (weekIndex in -monitoringWeeksAmount..0) {
            for (lesson in lessonsService.getAllLessons(weekIndex)) {
                val lessonIsFilled = lessonStateService.isLessonFilled(lesson.lesson, weekIndex)
                val lessonIsFinished = lessonStateService.isLessonFinished(lesson.lesson.id, weekIndex)

                haveAlerts = haveAlerts or (!lessonIsFilled && lessonIsFinished)
            }
        }

        return haveAlerts
    }
}