package com.bdev.hengschoolteacher.interactors.profile

import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.interactors.user_preferences.UserPreferencesInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import javax.inject.Inject

interface ProfileInteractor {
    fun getMe(): StaffMember?
}

class ProfileInteractorImpl @Inject constructor(
    private val userPreferencesInteractor: UserPreferencesInteractor,
    private val staffMembersStorageInteractor: StaffMembersStorageInteractor
): ProfileInteractor {
    override fun getMe(): StaffMember? {
        val login = userPreferencesInteractor.getUserLogin()

        return if (login != null) {
            staffMembersStorageInteractor.getStaffMember(login)
        } else {
            null
        }
    }
}
