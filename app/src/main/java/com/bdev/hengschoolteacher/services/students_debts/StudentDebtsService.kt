package com.bdev.hengschoolteacher.services.students_debts

import com.bdev.hengschoolteacher.services.students_payments.StudentsPaymentsProviderService
import com.bdev.hengschoolteacher.services.students_payments.StudentsPaymentsProviderServiceImpl
import com.bdev.hengschoolteacher.services.students_pricing.StudentsPricingService
import com.bdev.hengschoolteacher.services.students_pricing.StudentsPricingServiceImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean(scope = EBean.Scope.Singleton)
open class StudentDebtsService {
    @Bean(StudentsPaymentsProviderServiceImpl::class)
    lateinit var studentsPaymentsProviderService: StudentsPaymentsProviderService
    @Bean(StudentsPricingServiceImpl::class)
    lateinit var studentsPricingService: StudentsPricingService

    fun getExpectedDebt(studentLogin: String): Int {
        val payed = studentsPaymentsProviderService
                .getForStudent(studentLogin = studentLogin)
                .fold(0L) { amount, value -> amount + value.info.amount }
                .toInt()

        val price = studentsPricingService.getTotalExpectedPrice(
                studentLogin = studentLogin
        )

        return (price - payed).toInt()
    }

    fun getStudentDept(studentLogin: String): Int {
        val payed = studentsPaymentsProviderService
                .getForStudent(studentLogin = studentLogin)
                .fold(0L) { amount, value -> amount + value.info.amount }
                .toInt()

        val price = studentsPricingService.getTotalPrice(studentLogin)

        return (price - payed).toInt()
    }
}
