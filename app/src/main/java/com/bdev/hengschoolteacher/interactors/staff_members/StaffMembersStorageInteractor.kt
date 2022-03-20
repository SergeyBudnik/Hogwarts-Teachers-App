package com.bdev.hengschoolteacher.interactors.staff_members

import com.bdev.hengschoolteacher.dao.StaffMembersDao
import com.bdev.hengschoolteacher.dao.StaffMembersModel
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import javax.inject.Inject

interface StaffMembersStorageInteractor {
    fun getAllStaffMembers(): List<StaffMember>
    fun getStaffMember(login: String): StaffMember?
    fun setAllStaffMembers(staffMembers: List<StaffMember>)
}

class StaffMembersStorageInteractorImpl @Inject constructor(
    private val staffMembersDao: StaffMembersDao
): StaffMembersStorageInteractor {
    override fun getAllStaffMembers(): List<StaffMember> {
        return staffMembersDao.readValue().staffMembers
    }

    override fun getStaffMember(login: String): StaffMember? {
        return staffMembersDao.readValue().staffMembers.find { it.login == login }
    }

    override fun setAllStaffMembers(staffMembers: List<StaffMember>) {
        staffMembersDao.writeValue(StaffMembersModel(staffMembers))
    }
}
