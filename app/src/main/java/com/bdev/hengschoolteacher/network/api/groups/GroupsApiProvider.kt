package com.bdev.hengschoolteacher.network.api.groups

import com.bdev.hengschoolteacher.network.api_provider.AllApiProvider
import com.bdev.hengschoolteacher.network.api_provider.ApiProvider
import javax.inject.Inject

interface GroupsApiProvider : ApiProvider<GroupsApi>

class GroupsApiProviderImpl @Inject constructor(
    private val allApiProvider: AllApiProvider
) : GroupsApiProvider {
    override fun provide() = allApiProvider.provideGroupsApi()
}