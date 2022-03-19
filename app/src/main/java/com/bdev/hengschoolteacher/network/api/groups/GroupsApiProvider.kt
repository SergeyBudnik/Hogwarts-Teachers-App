package com.bdev.hengschoolteacher.network.api.groups

import com.bdev.hengschoolteacher.network.api_provider.AllApiProviderImpl
import com.bdev.hengschoolteacher.network.api_provider.ApiProvider
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface GroupsApiProvider : ApiProvider<GroupsApi>

@EBean
open class GroupsApiProviderImpl : GroupsApiProvider {
    @Bean
    lateinit var allApiProvider: AllApiProviderImpl

    override fun provide() = allApiProvider.provideGroupsApi()
}