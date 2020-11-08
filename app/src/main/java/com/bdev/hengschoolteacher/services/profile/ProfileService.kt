package com.bdev.hengschoolteacher.services.profile

import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.services.UserPreferencesService
import com.bdev.hengschoolteacher.services.staff.StaffMembersStorageService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class ProfileService {
    @Bean
    lateinit var userPreferencesService: UserPreferencesService
    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageService

    fun getMe(): StaffMember? {
        val login = userPreferencesService.getUserLogin()

        return if (login != null) {
            staffMembersStorageService.getStaffMember(login)
        } else {
            null
        }
    }
}
