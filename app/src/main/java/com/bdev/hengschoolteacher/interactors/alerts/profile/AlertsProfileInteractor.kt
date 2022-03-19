package com.bdev.hengschoolteacher.interactors.alerts.profile

import com.bdev.hengschoolteacher.interactors.students_debts.StudentDebtsService
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringTeachersInteractorImpl
import com.bdev.hengschoolteacher.interactors.profile.ProfileServiceImpl
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractorImpl
import com.bdev.hengschoolteacher.interactors.students_debts.StudentDebtsServiceImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface AlertsProfileInteractor {
    fun haveAlerts(): Boolean
    fun haveLessonsAlerts(): Boolean
    fun havePaymentsAlerts(): Boolean
    fun haveDebtsAlerts(): Boolean
}

@EBean
open class AlertsProfileInteractorImpl : AlertsProfileInteractor {
    @Bean
    lateinit var alertsMonitoringTeacherService: AlertsMonitoringTeachersInteractorImpl

    @Bean(StudentsStorageInteractorImpl::class)
    lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Bean(StudentDebtsServiceImpl::class)
    lateinit var studentsDebtsService: StudentDebtsService
    @Bean
    lateinit var profileService: ProfileServiceImpl

    override fun haveAlerts(): Boolean {
        return profileService.getMe()?.let { me ->
            alertsMonitoringTeacherService.haveAlerts(me.login)
        } ?: false
    }

    override fun haveLessonsAlerts(): Boolean {
        return profileService.getMe()?.let { me ->
            alertsMonitoringTeacherService.haveLessonsAlerts(me.login)
        } ?: false
    }

    override fun havePaymentsAlerts(): Boolean {
        return profileService.getMe()?.let { me ->
            alertsMonitoringTeacherService.havePaymentsAlerts(me.login)
        } ?: false
    }

    override fun haveDebtsAlerts(): Boolean {
        return profileService.getMe()?.let { me ->
            studentsStorageInteractor
                    .getAll()
                    .filter { it.managerLogin == me.login }
                    .any { student -> studentsDebtsService.getExpectedDebt(student.login) > 0 }
        } ?: false
    }
}
