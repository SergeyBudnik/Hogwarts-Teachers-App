package com.bdev.hengschoolteacher.rest

import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.rest.configuration.RestConfiguration
import com.bdev.hengschoolteacher.rest.converters.JsonConverter
import org.androidannotations.rest.spring.annotations.*
import org.androidannotations.rest.spring.api.RestClientHeaders
import org.androidannotations.rest.spring.api.RestClientSupport

@Rest(rootUrl = RestConfiguration.ROOT_URL, converters = [JsonConverter::class])
interface StudentsAttendancesRest : RestClientHeaders, RestClientSupport {
    @Get("/student-attendance")
    @RequiresHeader(RestConfiguration.HEADER_AUTHORIZATION)
    fun getStudentsAttendances(): List<StudentAttendance>

    @Post("/student-attendance")
    @RequiresHeader(RestConfiguration.HEADER_AUTHORIZATION)
    fun addStudentAttendance(@Body attendance: StudentAttendance)
}
