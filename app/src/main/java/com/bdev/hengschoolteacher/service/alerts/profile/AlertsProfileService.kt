package com.bdev.hengschoolteacher.service.alerts.profile

import com.bdev.hengschoolteacher.service.StudentPaymentsDeptService
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.service.alerts.monitoring.AlertsMonitoringTeachersService
import com.bdev.hengschoolteacher.service.profile.ProfileService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class AlertsProfileService {
    @Bean
    lateinit var alertsMonitoringTeacherService: AlertsMonitoringTeachersService

    @Bean
    lateinit var studentsService: StudentsService
    @Bean
    lateinit var studentsPaymentsDeptService: StudentPaymentsDeptService
    @Bean
    lateinit var profileService: ProfileService

    fun haveAlerts(): Boolean {
        return profileService.getMe()?.let { me ->
            alertsMonitoringTeacherService.haveAlerts(me.login)
        } ?: false
    }

    fun haveLessonsAlerts(): Boolean {
        return profileService.getMe()?.let { me ->
            alertsMonitoringTeacherService.haveLessonsAlerts(me.login)
        } ?: false
    }

    fun havePaymentsAlerts(): Boolean {
        return profileService.getMe()?.let { me ->
            alertsMonitoringTeacherService.havePaymentsAlerts(me.login)
        } ?: false
    }


    fun haveDebtsAlerts(): Boolean {
        return profileService.getMe()?.let { me ->
            studentsService
                    .getAllStudents()
                    .filter { it.managerLogin == me.login }
                    .any { student -> studentsPaymentsDeptService.getStudentDept(student.login) > 0 }
        } ?: false
    }
}
