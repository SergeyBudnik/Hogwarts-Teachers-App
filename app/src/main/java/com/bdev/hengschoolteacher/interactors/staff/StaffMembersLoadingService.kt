package com.bdev.hengschoolteacher.interactors.staff

import com.bdev.hengschoolteacher.async.CommonAsyncService
import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.rest.StaffMembersRest
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractorImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.rest.spring.annotations.RestService

interface StaffMembersLoadingService {
    fun load(): SmartPromise<Unit, Exception>
}

@EBean
open class StaffMembersLoadingServiceImpl : StaffMembersLoadingService, CommonAsyncService() {
    @RestService
    lateinit var staffMembersRest: StaffMembersRest

    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageServiceImpl

    @Bean
    lateinit var authService: AuthStorageInteractorImpl

    override fun load(): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(listOf(staffMembersRest), authService.getAuthInfo())

            staffMembersStorageService.setAllStaffMembers(
                    staffMembersRest.getAllStaffMembers()
            )
        }
    }
}
