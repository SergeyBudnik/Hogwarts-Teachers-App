package com.bdev.hengschoolteacher.rest

import com.bdev.hengschoolteacher.data.school.student.StudentPayment
import com.bdev.hengschoolteacher.rest.configuration.RestConfiguration
import com.bdev.hengschoolteacher.rest.converters.JsonConverter
import org.androidannotations.rest.spring.annotations.*
import org.androidannotations.rest.spring.api.RestClientHeaders
import org.androidannotations.rest.spring.api.RestClientSupport

@Rest(rootUrl = RestConfiguration.ROOT_URL, converters = [JsonConverter::class])
interface StudentsPaymentsRest : RestClientHeaders, RestClientSupport {
    @Get("/student-payment")
    @RequiresHeader(RestConfiguration.HEADER_AUTHORIZATION)
    fun getStudentsPayments(): List<StudentPayment>

    @Post("/student-payment")
    @RequiresHeader(RestConfiguration.HEADER_AUTHORIZATION)
    fun addStudentPayment(@Body payment: StudentPayment): Long?
}
