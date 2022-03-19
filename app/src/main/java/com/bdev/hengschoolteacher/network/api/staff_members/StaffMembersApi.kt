package com.bdev.hengschoolteacher.network.api.staff_members

import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import retrofit2.Call
import retrofit2.http.GET

interface StaffMembersApi {
    @GET("admin/staff/member/management")
    fun getAllStaffMembers(): Call<List<StaffMember>>
}
