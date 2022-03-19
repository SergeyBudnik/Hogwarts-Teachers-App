package com.bdev.hengschoolteacher.interactors.alerts.monitoring

import com.bdev.hengschoolteacher.interactors.LessonStateServiceImpl
import com.bdev.hengschoolteacher.interactors.students_debts.StudentDebtsService
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractorImpl
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractorImpl
import com.bdev.hengschoolteacher.interactors.students_debts.StudentDebtsServiceImpl
import com.bdev.hengschoolteacher.interactors.students_payments.StudentsPaymentsProviderService
import com.bdev.hengschoolteacher.interactors.students_payments.StudentsPaymentsProviderServiceImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface AlertsMonitoringTeachersInteractor {
    fun haveAlerts(teacherLogin: String): Boolean
    fun haveLessonsAlerts(teacherLogin: String): Boolean
    fun havePaymentsAlerts(teacherLogin: String): Boolean
    fun haveDebtsAlerts(teacherLogin: String): Boolean
}

@EBean
open class AlertsMonitoringTeachersInteractorImpl : AlertsMonitoringTeachersInteractor {
    private val monitoringWeeksAmount = 6

    @Bean(StudentsPaymentsProviderServiceImpl::class)
    lateinit var studentsPaymentsProviderService: StudentsPaymentsProviderService
    @Bean
    lateinit var lessonsService: LessonsInteractorImpl
    @Bean
    lateinit var lessonStateService: LessonStateServiceImpl
    @Bean(StudentsStorageInteractorImpl::class)
    lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Bean(StudentDebtsServiceImpl::class)
    lateinit var studentsDebtsService: StudentDebtsService

    override fun haveAlerts(teacherLogin: String): Boolean {
        return haveLessonsAlerts(teacherLogin) || havePaymentsAlerts(teacherLogin)
    }

    override fun haveLessonsAlerts(teacherLogin: String): Boolean {
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

    override fun havePaymentsAlerts(teacherLogin: String): Boolean {
        return studentsPaymentsProviderService.getForTeacher(
                teacherLogin = teacherLogin,
                onlyUnprocessed = true
        ).isNotEmpty()
    }

    override fun haveDebtsAlerts(teacherLogin: String): Boolean {
        return studentsStorageInteractor
                .getAll()
                .filter { it.managerLogin == teacherLogin }
                .any { student -> studentsDebtsService.getExpectedDebt(student.login) > 0 }
    }
}
