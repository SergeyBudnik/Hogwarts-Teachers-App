package com.bdev.hengschoolteacher.rest

import com.bdev.hengschoolteacher.data.school.requests.UserRequest
import com.bdev.hengschoolteacher.rest.configuration.RestConfiguration
import com.bdev.hengschoolteacher.rest.converters.JsonConverter
import org.androidannotations.rest.spring.annotations.Get
import org.androidannotations.rest.spring.annotations.RequiresHeader
import org.androidannotations.rest.spring.annotations.Rest
import org.androidannotations.rest.spring.api.RestClientHeaders
import org.androidannotations.rest.spring.api.RestClientSupport

@Rest(rootUrl = RestConfiguration.ROOT_URL, converters = [JsonConverter::class])
interface UsersRequestsRest : RestClientHeaders, RestClientSupport {
    @Get("/user-requests")
    @RequiresHeader(RestConfiguration.HEADER_AUTHORIZATION)
    fun getAllUsersRequests(): List<UserRequest>
}
