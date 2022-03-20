package com.bdev.hengschoolteacher.network.api.groups

import com.bdev.hengschoolteacher.data.school.group.Group
import retrofit2.Call
import retrofit2.http.GET

interface GroupsApi {
    @GET("groups")
    fun getAllGroups(): Call<List<Group>>
}