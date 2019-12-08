package com.bdev.hengschoolteacher.service.staff

import com.bdev.hengschoolteacher.async.CommonAsyncService
import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.rest.StaffMembersRest
import com.bdev.hengschoolteacher.service.AuthService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.rest.spring.annotations.RestService

@EBean
open class StaffMembersLoadingService : CommonAsyncService() {
    @RestService
    lateinit var staffMembersRest: StaffMembersRest

    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageService

    @Bean
    lateinit var authService: AuthService

    fun load(): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(listOf(staffMembersRest), authService.getAuthInfo())

            staffMembersStorageService.setAllStaffMembers(
                    staffMembersRest.getAllStaffMembers()
            )
        }
    }
}
