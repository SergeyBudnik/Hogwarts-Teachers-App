package com.bdev.hengschoolteacher.interactors.alerts.monitoring

import com.bdev.hengschoolteacher.interactors.students_debts.StudentDebtsService
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractorImpl
import com.bdev.hengschoolteacher.interactors.students_debts.StudentDebtsServiceImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface AlertsMonitoringStudentsInteractor {
    fun haveAlerts(): Boolean
}

@EBean
open class AlertsMonitoringStudentsInteractorImpl : AlertsMonitoringStudentsInteractor {
    @Bean(StudentsStorageInteractorImpl::class)
    lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Bean(StudentDebtsServiceImpl::class)
    lateinit var studentDebtsService: StudentDebtsService

    override fun haveAlerts(): Boolean {
        return studentsStorageInteractor
                .getAll()
                .map { studentDebtsService.getExpectedDebt(it.login) > 0 }
                .fold(false) { amount, value -> amount or value }
    }
}
