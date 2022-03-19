package com.bdev.hengschoolteacher.interactors.alerts.monitoring

import com.bdev.hengschoolteacher.interactors.staff.StaffMembersStorageServiceImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface AlertsMonitoringInteractor {
    fun haveAlerts(): Boolean
    fun lessonsHaveAlerts(): Boolean
    fun teachersHaveAlerts(): Boolean
    fun studentsHaveAlerts(): Boolean
}

@EBean
open class AlertsMonitoringInteractorImpl : AlertsMonitoringInteractor {
    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageServiceImpl

    @Bean
    lateinit var alertsMonitoringLessonsService: AlertsMonitoringLessonsInteractorImpl
    @Bean
    lateinit var alertsMonitoringTeachersService: AlertsMonitoringTeachersInteractorImpl
    @Bean
    lateinit var alertsMonitoringStudentsService: AlertsMonitoringStudentsInteractorImpl

    override fun haveAlerts(): Boolean {
        return lessonsHaveAlerts() || teachersHaveAlerts() || studentsHaveAlerts()
    }

    override fun lessonsHaveAlerts(): Boolean {
        return alertsMonitoringLessonsService.haveAlerts()
    }

    override fun teachersHaveAlerts(): Boolean {
        return staffMembersStorageService
                .getAllStaffMembers()
                .map { alertsMonitoringTeachersService.haveAlerts(it.login) }
                .fold(false) { result, value -> result or value }
    }

    override fun studentsHaveAlerts(): Boolean {
        return alertsMonitoringStudentsService.haveAlerts()
    }
}
