package com.bdev.hengschoolteacher.services.students_payments

import com.bdev.hengschoolteacher.data.school.student_payment.ExistingStudentPayment
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface StudentsPaymentsModifierService {
    fun setAll(payments: List<ExistingStudentPayment>)
    fun add(payment: ExistingStudentPayment)
}

@EBean(scope = EBean.Scope.Singleton)
open class StudentsPaymentsModifierServiceImpl : StudentsPaymentsModifierService {
    @Bean(StudentsPaymentsStorageServiceImpl::class)
    lateinit var studentsPaymentsStorageService: StudentsPaymentsStorageService

    override fun setAll(payments: List<ExistingStudentPayment>) {
        studentsPaymentsStorageService.setAll(payments = payments)
    }

    override fun add(payment: ExistingStudentPayment) {
        studentsPaymentsStorageService.setAll(
                payments = studentsPaymentsStorageService.getAll().plus(payment)
        )
    }
}