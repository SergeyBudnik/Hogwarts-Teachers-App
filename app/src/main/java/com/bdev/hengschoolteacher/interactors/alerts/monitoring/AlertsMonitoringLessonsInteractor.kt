package com.bdev.hengschoolteacher.interactors.alerts.monitoring

import com.bdev.hengschoolteacher.interactors.LessonStateServiceImpl
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractorImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface AlertsMonitoringLessonsInteractor {
    fun haveAlerts(): Boolean
}

@EBean
open class AlertsMonitoringLessonsInteractorImpl : AlertsMonitoringLessonsInteractor {
    private val monitoringWeeksAmount = 6

    @Bean
    lateinit var lessonsService: LessonsInteractorImpl
    @Bean
    lateinit var lessonStateService: LessonStateServiceImpl

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