package com.bdev.hengschoolteacher.service

import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class StudentPaymentsDeptService {
    @Bean
    lateinit var studentPaymentsService: StudentsPaymentsService
    @Bean
    lateinit var studentPriceService: StudentPriceService

    fun getStudentDept(studentLogin: String): Int {
        val payed = studentPaymentsService
                .getPayments(studentLogin)
                .fold(0L) { amount, value -> amount + value.info.amount }
                .toInt()

        val price = studentPriceService.getTotalPrice(studentLogin)

        return (price - payed).toInt()
    }
}
