package com.bdev.hengschoolteacher.service

import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class StudentPaymentsDeptService {
    @Bean
    lateinit var studentPaymentsService: StudentsPaymentsService
    @Bean
    lateinit var studentPriceService: StudentPriceService

    fun getStudentDept(studentId: Long): Int {
        val payed = studentPaymentsService
                .getPayments(studentId)
                .fold(0L) { amount, value -> amount + value.amount }
                .toInt()

        val price = studentPriceService.getTotalPrice(studentId)

        return (price - payed).toInt()
    }
}
