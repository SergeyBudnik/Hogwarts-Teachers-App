package com.bdev.hengschoolteacher.interactors.alerts.profile

import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringTeachersInteractor
import com.bdev.hengschoolteacher.interactors.profile.ProfileInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_debts.StudentsDebtsInteractor
import javax.inject.Inject

interface AlertsProfileInteractor {
    fun haveAlerts(): Boolean
    fun haveLessonsAlerts(): Boolean
    fun havePaymentsAlerts(): Boolean
    fun haveDebtsAlerts(): Boolean
}

class AlertsProfileInteractorImpl @Inject constructor(
    private val alertsMonitoringTeacherService: AlertsMonitoringTeachersInteractor,
    private val studentsStorageInteractor: StudentsStorageInteractor,
    private val studentsDebtsInteractor: StudentsDebtsInteractor,
    private val profileInteractor: ProfileInteractor
): AlertsProfileInteractor {
    override fun haveAlerts(): Boolean {
        return profileInteractor.getMe()?.let { me ->
            alertsMonitoringTeacherService.haveAlerts(me.login)
        } ?: false
    }

    override fun haveLessonsAlerts(): Boolean {
        return profileInteractor.getMe()?.let { me ->
            alertsMonitoringTeacherService.haveLessonsAlerts(me.login)
        } ?: false
    }

    override fun havePaymentsAlerts(): Boolean {
        return profileInteractor.getMe()?.let { me ->
            alertsMonitoringTeacherService.havePaymentsAlerts(me.login)
        } ?: false
    }

    override fun haveDebtsAlerts(): Boolean {
        return profileInteractor.getMe()?.let { me ->
            studentsStorageInteractor
                    .getAll()
                    .filter { it.managerLogin == me.login }
                    .any { student -> studentsDebtsInteractor.getExpectedDebt(student.login) > 0 }
        } ?: false
    }
}
