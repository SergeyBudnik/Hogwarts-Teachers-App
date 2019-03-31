package com.bdev.hengschoolteacher.rest

import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import com.bdev.hengschoolteacher.rest.configuration.RestConfiguration
import com.bdev.hengschoolteacher.rest.converters.JsonConverter
import org.androidannotations.rest.spring.annotations.*
import org.androidannotations.rest.spring.api.RestClientHeaders
import org.androidannotations.rest.spring.api.RestClientSupport

@Rest(rootUrl = RestConfiguration.ROOT_URL, converters = [JsonConverter::class])
interface LessonStatusRest : RestClientHeaders, RestClientSupport {
    @Get("/lesson-status/{from}/{to}")
    @RequiresHeader(RestConfiguration.HEADER_AUTHORIZATION)
    fun getAllLessonsStatuses(@Path from: Long, @Path to: Long): List<LessonStatus>

    @Post("/lesson-status")
    @RequiresHeader(RestConfiguration.HEADER_AUTHORIZATION)
    fun addLessonStatus(@Body lessonStatus: LessonStatus): Long?
}
