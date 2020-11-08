package com.bdev.hengschoolteacher.services.alerts.monitoring

import com.bdev.hengschoolteacher.services.LessonStateService
import com.bdev.hengschoolteacher.services.StudentPaymentsDeptService
import com.bdev.hengschoolteacher.services.lessons.LessonsService
import com.bdev.hengschoolteacher.services.students.StudentsStorageService
import com.bdev.hengschoolteacher.services.students.StudentsStorageServiceImpl
import com.bdev.hengschoolteacher.services.students_payments.StudentsPaymentsProviderService
import com.bdev.hengschoolteacher.services.students_payments.StudentsPaymentsProviderServiceImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class AlertsMonitoringTeachersService {
    private val monitoringWeeksAmount = 6

    @Bean(StudentsPaymentsProviderServiceImpl::class)
    lateinit var studentsPaymentsProviderService: StudentsPaymentsProviderService
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var lessonStateService: LessonStateService
    @Bean(StudentsStorageServiceImpl::class)
    lateinit var studentsStorageService: StudentsStorageService
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
        return studentsPaymentsProviderService.getForTeacher(
                teacherLogin = teacherLogin,
                onlyUnprocessed = true
        ).isNotEmpty()
    }

    fun haveDebtsAlerts(teacherLogin: String): Boolean {
        return studentsStorageService
                .getAll()
                .filter { it.managerLogin == teacherLogin }
                .any { student -> studentsPaymentsDeptService.getStudentDept(student.login) > 0 }
    }
}
