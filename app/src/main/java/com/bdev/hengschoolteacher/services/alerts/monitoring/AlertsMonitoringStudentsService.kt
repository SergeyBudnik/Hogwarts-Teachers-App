package com.bdev.hengschoolteacher.services.alerts.monitoring

import com.bdev.hengschoolteacher.services.StudentPaymentsDeptService
import com.bdev.hengschoolteacher.services.students.StudentsStorageService
import com.bdev.hengschoolteacher.services.students.StudentsStorageServiceImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class AlertsMonitoringStudentsService {
    @Bean(StudentsStorageServiceImpl::class)
    lateinit var studentsStorageService: StudentsStorageService
    @Bean
    lateinit var studentPaymentsDeptService: StudentPaymentsDeptService

    fun haveAlerts(): Boolean {
        return studentsStorageService
                .getAll()
                .map { studentPaymentsDeptService.getStudentDept(it.login) > 0 }
                .fold(false) { amount, value -> amount or value }
    }
}
