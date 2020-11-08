package com.bdev.hengschoolteacher.services.alerts.profile

import com.bdev.hengschoolteacher.services.StudentPaymentsDeptService
import com.bdev.hengschoolteacher.services.students.StudentsStorageService
import com.bdev.hengschoolteacher.services.alerts.monitoring.AlertsMonitoringTeachersService
import com.bdev.hengschoolteacher.services.profile.ProfileService
import com.bdev.hengschoolteacher.services.students.StudentsStorageServiceImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class AlertsProfileService {
    @Bean
    lateinit var alertsMonitoringTeacherService: AlertsMonitoringTeachersService

    @Bean(StudentsStorageServiceImpl::class)
    lateinit var studentsStorageService: StudentsStorageService
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
            studentsStorageService
                    .getAll()
                    .filter { it.managerLogin == me.login }
                    .any { student -> studentsPaymentsDeptService.getStudentDept(student.login) > 0 }
        } ?: false
    }
}
