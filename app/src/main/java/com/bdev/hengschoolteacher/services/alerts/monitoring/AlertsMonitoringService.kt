package com.bdev.hengschoolteacher.services.alerts.monitoring

import com.bdev.hengschoolteacher.services.staff.StaffMembersStorageService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class AlertsMonitoringService {
    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageService

    @Bean
    lateinit var alertsMonitoringLessonsService: AlertsMonitoringLessonsService
    @Bean
    lateinit var alertsMonitoringTeachersService: AlertsMonitoringTeachersService
    @Bean
    lateinit var alertsMonitoringStudentsService: AlertsMonitoringStudentsService

    fun haveAlerts(): Boolean {
        return lessonsHaveAlerts() || teachersHaveAlerts() || studentsHaveAlerts()
    }

    fun lessonsHaveAlerts(): Boolean {
        return alertsMonitoringLessonsService.haveAlerts()
    }

    fun teachersHaveAlerts(): Boolean {
        return staffMembersStorageService
                .getAllStaffMembers()
                .map { alertsMonitoringTeachersService.haveAlerts(it.login) }
                .fold(false) { result, value -> result or value }
    }

    fun studentsHaveAlerts(): Boolean {
        return alertsMonitoringStudentsService.haveAlerts()
    }
}
