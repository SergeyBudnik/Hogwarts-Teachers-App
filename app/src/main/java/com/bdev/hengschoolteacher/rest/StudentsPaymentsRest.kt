package com.bdev.hengschoolteacher.rest

import com.bdev.hengschoolteacher.data.school.student_payment.StudentPayment
import com.bdev.hengschoolteacher.data.school.student_payment.StudentPaymentInfo
import com.bdev.hengschoolteacher.rest.configuration.RestConfiguration
import com.bdev.hengschoolteacher.rest.converters.JsonConverter
import org.androidannotations.rest.spring.annotations.*
import org.androidannotations.rest.spring.api.RestClientHeaders
import org.androidannotations.rest.spring.api.RestClientSupport

@Rest(rootUrl = "${RestConfiguration.ROOT_URL}/student-payment", converters = [JsonConverter::class])
interface StudentsPaymentsRest : RestClientHeaders, RestClientSupport {
    @Get("")
    @RequiresHeader(RestConfiguration.HEADER_AUTHORIZATION)
    fun getStudentsPayments(): List<StudentPayment>

    @Post("")
    @RequiresHeader(RestConfiguration.HEADER_AUTHORIZATION)
    fun addStudentPayment(@Body payment: StudentPaymentInfo): Long?

    @Put("/processed/{paymentId}/{processed}")
    @RequiresHeader(RestConfiguration.HEADER_AUTHORIZATION)
    fun setStudentPaymentsProcessed(@Path paymentId: Long, @Path processed: Boolean)
}
