package com.bdev.hengschoolteacher.rest

import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.rest.configuration.RestConfiguration
import com.bdev.hengschoolteacher.rest.converters.JsonConverter
import org.androidannotations.rest.spring.annotations.Get
import org.androidannotations.rest.spring.annotations.RequiresHeader
import org.androidannotations.rest.spring.annotations.Rest
import org.androidannotations.rest.spring.api.RestClientHeaders

@Rest(rootUrl = RestConfiguration.ROOT_URL, converters = [JsonConverter::class])
interface StaffMembersRest : RestClientHeaders {
    @Get("/admin/staff/member/management")
    @RequiresHeader(RestConfiguration.HEADER_AUTHORIZATION)
    fun getAllStaffMembers(): List<StaffMember>
}
