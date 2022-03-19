package com.bdev.hengschoolteacher.interactors.staff

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractorImpl
import com.bdev.hengschoolteacher.network.api.staff_members.StaffMembersApiProviderImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface StaffMembersLoadingService {
    fun load(): SmartPromise<Unit, Exception>
}

@EBean
open class StaffMembersLoadingServiceImpl : StaffMembersLoadingService {
    @Bean
    lateinit var staffMembersApiProvider: StaffMembersApiProviderImpl
    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageServiceImpl

    @Bean
    lateinit var authService: AuthStorageInteractorImpl

    override fun load(): SmartPromise<Unit, Exception> {
        return smartTask {
            staffMembersStorageService.setAllStaffMembers(
                staffMembersApiProvider.provide().getAllStaffMembers().execute().body()!!
            )
        }
    }
}
