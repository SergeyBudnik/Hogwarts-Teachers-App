package com.bdev.hengschoolteacher.service.alerts.monitoring

import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class MonitoringAlertsService {
    @Bean
    lateinit var monitoringAlertsLessonsService: MonitoringAlertsLessonsService
    @Bean
    lateinit var monitoringAlertsTeachersService: MonitoringAlertsTeachersService
    @Bean
    lateinit var monitoringAlertsStudentsService: MonitoringAlertsStudentsService

    fun haveAlerts(): Boolean {
        return lessonsHaveAlerts() || teachersHaveAlerts() || studentsHaveAlerts()
    }

    fun lessonsHaveAlerts(): Boolean {
        return monitoringAlertsLessonsService.haveAlerts()
    }

    fun teachersHaveAlerts(): Boolean {
        return monitoringAlertsTeachersService.haveAlerts()
    }

    fun studentsHaveAlerts(): Boolean {
        return monitoringAlertsStudentsService.haveAlerts()
    }
}
