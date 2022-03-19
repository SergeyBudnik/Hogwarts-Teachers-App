package com.bdev.hengschoolteacher.network.api.groups

import com.bdev.hengschoolteacher.data.school.group.Group
import com.bdev.hengschoolteacher.network.api_provider.AllApiProvider
import com.bdev.hengschoolteacher.network.api_provider.AllApiProviderImpl
import com.bdev.hengschoolteacher.network.api_provider.ApiProvider
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import retrofit2.Call
import retrofit2.http.GET

interface GroupsApi {
    @GET("groups")
    fun getAllGroups(): Call<List<Group>>
}