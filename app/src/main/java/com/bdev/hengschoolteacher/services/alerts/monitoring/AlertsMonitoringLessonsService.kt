package com.bdev.hengschoolteacher.services.alerts.monitoring

import com.bdev.hengschoolteacher.services.LessonStateService
import com.bdev.hengschoolteacher.services.lessons.LessonsService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class AlertsMonitoringLessonsService {
    private val monitoringWeeksAmount = 6

    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var lessonStateService: LessonStateService

    fun haveAlerts(): Boolean {
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