package com.bdev.hengschoolteacher.rest

import com.bdev.hengschoolteacher.data.school.student_payment.ExistingStudentPayment
import com.bdev.hengschoolteacher.data.school.student_payment.NewStudentPayment
import com.bdev.hengschoolteacher.rest.configuration.RestConfiguration
import com.bdev.hengschoolteacher.rest.converters.JsonConverter
import org.androidannotations.rest.spring.annotations.*
import org.androidannotations.rest.spring.api.RestClientHeaders
import org.androidannotations.rest.spring.api.RestClientSupport

@Rest(rootUrl = "${RestConfiguration.ROOT_URL}/admin/students/payments", converters = [JsonConverter::class])
interface StudentsPaymentsRest : RestClientHeaders, RestClientSupport {
    @Get("")
    @RequiresHeader(RestConfiguration.HEADER_AUTHORIZATION)
    fun getStudentsPayments(): List<ExistingStudentPayment>

    @Post("")
    @RequiresHeader(RestConfiguration.HEADER_AUTHORIZATION)
    fun addStudentPayment(@Body paymentNew: NewStudentPayment): Long?

    @Put("/{paymentId}/processed/{processed}")
    @RequiresHeader(RestConfiguration.HEADER_AUTHORIZATION)
    fun setStudentPaymentsProcessed(@Path paymentId: Long, @Path processed: Boolean)
}
