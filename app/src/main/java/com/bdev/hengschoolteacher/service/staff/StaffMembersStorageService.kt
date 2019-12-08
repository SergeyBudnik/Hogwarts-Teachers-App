package com.bdev.hengschoolteacher.service.staff

import com.bdev.hengschoolteacher.dao.StaffMembersDao
import com.bdev.hengschoolteacher.dao.StaffMembersModel
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class StaffMembersStorageService {
    @Bean
    lateinit var staffMembersDao: StaffMembersDao

    fun getAllStaffMembers(): List<StaffMember> {
        return staffMembersDao.readValue().staffMembers
    }

    fun getStaffMember(login: String): StaffMember? {
        return staffMembersDao.readValue().staffMembers.find { it.login == login }
    }

    fun setAllStaffMembers(staffMembers: List<StaffMember>) {
        staffMembersDao.writeValue(StaffMembersModel(staffMembers))
    }
}
