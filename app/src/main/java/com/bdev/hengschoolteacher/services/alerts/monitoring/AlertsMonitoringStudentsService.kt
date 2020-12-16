package com.bdev.hengschoolteacher.services.alerts.monitoring

import com.bdev.hengschoolteacher.services.students_debts.StudentDebtsService
import com.bdev.hengschoolteacher.services.students.StudentsStorageService
import com.bdev.hengschoolteacher.services.students.StudentsStorageServiceImpl
import com.bdev.hengschoolteacher.services.students_debts.StudentDebtsServiceImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class AlertsMonitoringStudentsService {
    @Bean(StudentsStorageServiceImpl::class)
    lateinit var studentsStorageService: StudentsStorageService
    @Bean(StudentDebtsServiceImpl::class)
    lateinit var studentDebtsService: StudentDebtsService

    fun haveAlerts(): Boolean {
        return studentsStorageService
                .getAll()
                .map { studentDebtsService.getExpectedDebt(it.login) > 0 }
                .fold(false) { amount, value -> amount or value }
    }
}
