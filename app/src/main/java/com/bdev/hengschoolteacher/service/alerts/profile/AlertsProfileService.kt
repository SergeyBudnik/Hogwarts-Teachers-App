package com.bdev.hengschoolteacher.service.alerts.profile

import com.bdev.hengschoolteacher.data.school.teacher.Teacher
import com.bdev.hengschoolteacher.service.UserPreferencesService
import com.bdev.hengschoolteacher.service.alerts.monitoring.AlertsMonitoringTeachersService
import com.bdev.hengschoolteacher.service.teacher.TeacherStorageService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class AlertsProfileService {
    @Bean
    lateinit var alertsMonitoringTeacherService: AlertsMonitoringTeachersService

    @Bean
    lateinit var userPreferencesService: UserPreferencesService
    @Bean
    lateinit var teacherStorageService: TeacherStorageService

    fun haveAlerts(): Boolean {
        return getMe()?.let { alertsMonitoringTeacherService.haveAlerts(it.id) } ?: false
    }

    fun haveLessonsAlerts(): Boolean {
        return getMe()?.let { alertsMonitoringTeacherService.haveLessonsAlerts(it.id) } ?: false
    }

    fun havePaymentsAlerts(): Boolean {
        return getMe()?.let { alertsMonitoringTeacherService.havePaymentsAlerts(it.id) } ?: false
    }

    private fun getMe(): Teacher? {
        return userPreferencesService.getUserLogin()?.let { login ->
            teacherStorageService.getTeacherByLogin(login)
        }
    }
}
