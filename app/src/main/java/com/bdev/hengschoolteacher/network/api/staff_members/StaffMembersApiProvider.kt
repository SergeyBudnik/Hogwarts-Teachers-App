package com.bdev.hengschoolteacher.network.api.staff_members

import com.bdev.hengschoolteacher.network.api_provider.AllApiProviderImpl
import com.bdev.hengschoolteacher.network.api_provider.ApiProvider
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface StaffMembersApiProvider : ApiProvider<StaffMembersApi>

@EBean
open class StaffMembersApiProviderImpl : StaffMembersApiProvider {
    @Bean
    lateinit var allApiProvider: AllApiProviderImpl

    override fun provide() = allApiProvider.provideStaffMembersApi()
}