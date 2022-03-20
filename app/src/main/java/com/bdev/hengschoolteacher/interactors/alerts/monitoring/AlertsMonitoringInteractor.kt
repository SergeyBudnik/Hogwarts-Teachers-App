package com.bdev.hengschoolteacher.interactors.alerts.monitoring

import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import javax.inject.Inject

interface AlertsMonitoringInteractor {
    fun haveAlerts(): Boolean
    fun lessonsHaveAlerts(): Boolean
    fun teachersHaveAlerts(): Boolean
    fun studentsHaveAlerts(): Boolean
}

class AlertsMonitoringInteractorImpl @Inject constructor(
    private val staffMembersStorageInteractor: StaffMembersStorageInteractor,
    private val alertsMonitoringLessonsService: AlertsMonitoringLessonsInteractor,
    private val alertsMonitoringTeachersService: AlertsMonitoringTeachersInteractor,
    private val alertsMonitoringStudentsService: AlertsMonitoringStudentsInteractor
): AlertsMonitoringInteractor {
    override fun haveAlerts(): Boolean {
        return lessonsHaveAlerts() || teachersHaveAlerts() || studentsHaveAlerts()
    }

    override fun lessonsHaveAlerts(): Boolean {
        return alertsMonitoringLessonsService.haveAlerts()
    }

    override fun teachersHaveAlerts(): Boolean {
        return staffMembersStorageInteractor
                .getAllStaffMembers()
                .map { alertsMonitoringTeachersService.haveAlerts(it.login) }
                .fold(false) { result, value -> result or value }
    }

    override fun studentsHaveAlerts(): Boolean {
        return alertsMonitoringStudentsService.haveAlerts()
    }
}
