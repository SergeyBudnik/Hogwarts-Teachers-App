package com.bdev.hengschoolteacher.network.api.staff_members

import com.bdev.hengschoolteacher.network.api_provider.AllApiProvider
import com.bdev.hengschoolteacher.network.api_provider.ApiProvider
import javax.inject.Inject

interface StaffMembersApiProvider : ApiProvider<StaffMembersApi>

class StaffMembersApiProviderImpl @Inject constructor(
    private val allApiProvider: AllApiProvider
): StaffMembersApiProvider {
    override fun provide() = allApiProvider.provideStaffMembersApi()
}