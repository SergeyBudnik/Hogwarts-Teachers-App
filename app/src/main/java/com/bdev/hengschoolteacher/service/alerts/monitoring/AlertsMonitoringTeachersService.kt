package com.bdev.hengschoolteacher.service.alerts.monitoring

import com.bdev.hengschoolteacher.service.*
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class AlertsMonitoringTeachersService {
    private val monitoringWeeksAmount = 6

    @Bean
    lateinit var studentsPaymentsService: StudentsPaymentsService
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var lessonStateService: LessonStateService
    @Bean
    lateinit var studentsService: StudentsService
    @Bean
    lateinit var studentsPaymentsDeptService: StudentPaymentsDeptService

    fun haveAlerts(teacherLogin: String): Boolean {
        return haveLessonsAlerts(teacherLogin) || havePaymentsAlerts(teacherLogin)
    }

    fun haveLessonsAlerts(teacherLogin: String): Boolean {
        var haveAlerts = false

        for (weekIndex in -monitoringWeeksAmount..0) {
            for (lesson in lessonsService.getTeacherLessons(teacherLogin, weekIndex)) {
                val lessonIsFilled = lessonStateService.isLessonFilled(lesson.lesson, weekIndex)
                val lessonIsFinished = lessonStateService.isLessonFinished(lesson.lesson.id, weekIndex)

                haveAlerts = haveAlerts or (!lessonIsFilled and lessonIsFinished)
            }
        }

        return haveAlerts
    }

    fun havePaymentsAlerts(teacherLogin: String): Boolean {
        return studentsPaymentsService.getPaymentsToTeacher(teacherLogin, true).isNotEmpty()
    }

    fun haveDebtsAlerts(teacherLogin: String): Boolean {
        return studentsService
                .getAllStudents()
                .filter { it.managerLogin == teacherLogin }
                .any { student -> studentsPaymentsDeptService.getStudentDept(student.login) > 0 }
    }
}
