package com.bdev.hengschoolteacher.rest

import com.bdev.hengschoolteacher.data.school.lesson.LessonTransfer
import com.bdev.hengschoolteacher.rest.configuration.RestConfiguration
import com.bdev.hengschoolteacher.rest.converters.JsonConverter
import org.androidannotations.rest.spring.annotations.Get
import org.androidannotations.rest.spring.annotations.Post
import org.androidannotations.rest.spring.annotations.RequiresHeader
import org.androidannotations.rest.spring.annotations.Rest
import org.androidannotations.rest.spring.api.RestClientHeaders
import org.androidannotations.rest.spring.api.RestClientSupport

@Rest(rootUrl = "${RestConfiguration.ROOT_URL}/lesson-transfer", converters = [JsonConverter::class])
interface LessonsTransferRest : RestClientHeaders, RestClientSupport {
    @Get("")
    @RequiresHeader(RestConfiguration.HEADER_AUTHORIZATION)
    fun getAllLessonsTransfers(): List<LessonTransfer>

    @Post("")
    @RequiresHeader(RestConfiguration.HEADER_AUTHORIZATION)
    fun addLessonTransfer(): Long?
}
