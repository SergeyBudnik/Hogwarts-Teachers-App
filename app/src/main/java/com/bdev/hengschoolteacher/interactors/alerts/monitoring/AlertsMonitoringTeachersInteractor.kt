package com.bdev.hengschoolteacher.interactors.alerts.monitoring

import com.bdev.hengschoolteacher.interactors.LessonStateService
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_debts.StudentsDebtsInteractor
import com.bdev.hengschoolteacher.interactors.students_payments.StudentsPaymentsProviderInteractor
import javax.inject.Inject

interface AlertsMonitoringTeachersInteractor {
    fun haveAlerts(teacherLogin: String): Boolean
    fun haveLessonsAlerts(teacherLogin: String): Boolean
    fun havePaymentsAlerts(teacherLogin: String): Boolean
    fun haveDebtsAlerts(teacherLogin: String): Boolean
}

class AlertsMonitoringTeachersInteractorImpl @Inject constructor(
    private val studentsPaymentsProviderInteractor: StudentsPaymentsProviderInteractor,
    private val lessonsService: LessonsInteractor,
    private val lessonStateService: LessonStateService,
    private val studentsStorageInteractor: StudentsStorageInteractor,
    private val studentsDebtsInteractor: StudentsDebtsInteractor
): AlertsMonitoringTeachersInteractor {
    private val monitoringWeeksAmount = 6

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
        return studentsPaymentsProviderInteractor.getForTeacher(
                teacherLogin = teacherLogin,
                onlyUnprocessed = true
        ).isNotEmpty()
    }

    override fun haveDebtsAlerts(teacherLogin: String): Boolean {
        return studentsStorageInteractor
                .getAll()
                .filter { it.managerLogin == teacherLogin }
                .any { student -> studentsDebtsInteractor.getExpectedDebt(student.login) > 0 }
    }
}
