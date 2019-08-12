package com.bdev.hengschoolteacher.service.alerts.monitoring

import com.bdev.hengschoolteacher.service.LessonStateService
import com.bdev.hengschoolteacher.service.LessonsService
import com.bdev.hengschoolteacher.service.StudentsPaymentsService
import com.bdev.hengschoolteacher.service.teacher.TeacherStorageService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class AlertsMonitoringTeachersService {
    private val monitoringWeeksAmount = 6

    @Bean
    lateinit var teacherStorageService: TeacherStorageService
    @Bean
    lateinit var studentsPaymentsService: StudentsPaymentsService
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var lessonStateService: LessonStateService

    fun haveAlerts(teacherId: Long): Boolean {
        return haveLessonsAlerts(teacherId) || havePaymentsAlerts(teacherId)
    }

    fun haveLessonsAlerts(teacherId: Long): Boolean {
        var haveAlerts = false

        for (weekIndex in -monitoringWeeksAmount..0) {
            for (lesson in lessonsService.getTeacherLessons(teacherId, weekIndex)) {
                val lessonIsFilled = lessonStateService.isLessonFilled(lesson.lesson, weekIndex)

                haveAlerts = haveAlerts or !lessonIsFilled
            }
        }

        return haveAlerts
    }

    fun havePaymentsAlerts(teacherId: Long): Boolean {
        return studentsPaymentsService.getPaymentsToTeacher(teacherId, true).isNotEmpty()
    }
}
