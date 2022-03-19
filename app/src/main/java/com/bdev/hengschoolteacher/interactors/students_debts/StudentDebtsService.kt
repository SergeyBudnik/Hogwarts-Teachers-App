package com.bdev.hengschoolteacher.interactors.students_debts

import com.bdev.hengschoolteacher.interactors.students_payments.StudentsPaymentsProviderService
import com.bdev.hengschoolteacher.interactors.students_payments.StudentsPaymentsProviderServiceImpl
import com.bdev.hengschoolteacher.interactors.students_pricing.StudentsPricingService
import com.bdev.hengschoolteacher.interactors.students_pricing.StudentsPricingServiceImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface StudentDebtsService {
    fun getExpectedDebt(studentLogin: String): Int
    fun getDebt(studentLogin: String): Int
}

@EBean(scope = EBean.Scope.Singleton)
open class StudentDebtsServiceImpl : StudentDebtsService {
    @Bean(StudentsPaymentsProviderServiceImpl::class)
    lateinit var studentsPaymentsProviderService: StudentsPaymentsProviderService
    @Bean(StudentsPricingServiceImpl::class)
    lateinit var studentsPricingService: StudentsPricingService

    override fun getExpectedDebt(studentLogin: String): Int {
        val payed = getPaymentsAmount(studentLogin = studentLogin)
        val price = studentsPricingService.getTotalExpectedPrice(studentLogin = studentLogin)

        return (price - payed).toInt()
    }

    override fun getDebt(studentLogin: String): Int {
        val payed = getPaymentsAmount(studentLogin = studentLogin)
        val price = studentsPricingService.getTotalPrice(studentLogin = studentLogin)

        return (price - payed).toInt()
    }

    private fun getPaymentsAmount(studentLogin: String): Int {
        return studentsPaymentsProviderService
                .getForStudent(studentLogin = studentLogin)
                .fold(0L) { amount, value -> amount + value.info.amount }
                .toInt()
    }
}
