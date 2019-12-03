package com.bdev.hengschoolteacher.service.alerts.profile

import com.bdev.hengschoolteacher.service.alerts.monitoring.AlertsMonitoringTeachersService
import com.bdev.hengschoolteacher.service.profile.ProfileService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class AlertsProfileService {
    @Bean
    lateinit var alertsMonitoringTeacherService: AlertsMonitoringTeachersService

    @Bean
    lateinit var profileService: ProfileService

    fun haveAlerts(): Boolean {
        return profileService.getMe()?.let {
            alertsMonitoringTeacherService.haveAlerts(it.login)
        } ?: false
    }

    fun haveLessonsAlerts(): Boolean {
        return profileService.getMe()?.let {
            alertsMonitoringTeacherService.haveLessonsAlerts(it.login)
        } ?: false
    }

    fun havePaymentsAlerts(): Boolean {
        return profileService.getMe()?.let {
            alertsMonitoringTeacherService.havePaymentsAlerts(it.login)
        } ?: false
    }
}
