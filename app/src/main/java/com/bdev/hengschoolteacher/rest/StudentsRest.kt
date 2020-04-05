package com.bdev.hengschoolteacher.rest

import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.rest.configuration.RestConfiguration
import com.bdev.hengschoolteacher.rest.converters.JsonConverter
import org.androidannotations.rest.spring.annotations.Get
import org.androidannotations.rest.spring.annotations.RequiresHeader
import org.androidannotations.rest.spring.annotations.Rest
import org.androidannotations.rest.spring.api.RestClientHeaders
import org.androidannotations.rest.spring.api.RestClientSupport

@Rest(rootUrl = RestConfiguration.ROOT_URL, converters = [JsonConverter::class])
interface StudentsRest : RestClientHeaders, RestClientSupport {
    @Get("/admin/students/management")
    @RequiresHeader(RestConfiguration.HEADER_AUTHORIZATION)
    fun getAllStudents(): List<Student>
}
