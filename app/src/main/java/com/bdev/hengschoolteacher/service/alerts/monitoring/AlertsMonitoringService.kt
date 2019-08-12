package com.bdev.hengschoolteacher.service.alerts.monitoring

import com.bdev.hengschoolteacher.service.teacher.TeacherStorageService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class AlertsMonitoringService {
    @Bean
    lateinit var teacherStorageService: TeacherStorageService

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
        return teacherStorageService
                .getAllTeachers()
                .map { alertsMonitoringTeachersService.haveAlerts(it.id) }
                .fold(false) { result, value -> result or value }
    }

    fun studentsHaveAlerts(): Boolean {
        return alertsMonitoringStudentsService.haveAlerts()
    }
}
