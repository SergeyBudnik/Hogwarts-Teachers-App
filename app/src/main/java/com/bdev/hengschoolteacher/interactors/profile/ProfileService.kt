package com.bdev.hengschoolteacher.interactors.profile

import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.interactors.UserPreferencesServiceImpl
import com.bdev.hengschoolteacher.interactors.staff.StaffMembersStorageServiceImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface ProfileService {
    fun getMe(): StaffMember?
}

@EBean
open class ProfileServiceImpl : ProfileService {
    @Bean
    lateinit var userPreferencesService: UserPreferencesServiceImpl
    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageServiceImpl

    override fun getMe(): StaffMember? {
        val login = userPreferencesService.getUserLogin()

        return if (login != null) {
            staffMembersStorageService.getStaffMember(login)
        } else {
            null
        }
    }
}
