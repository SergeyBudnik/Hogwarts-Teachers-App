package com.bdev.hengschoolteacher.interactors.staff

import com.bdev.hengschoolteacher.dao.StaffMembersDaoImpl
import com.bdev.hengschoolteacher.dao.StaffMembersModel
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface StaffMembersStorageService {
    fun getAllStaffMembers(): List<StaffMember>
    fun getStaffMember(login: String): StaffMember?
    fun setAllStaffMembers(staffMembers: List<StaffMember>)
}

@EBean
open class StaffMembersStorageServiceImpl : StaffMembersStorageService {
    @Bean
    lateinit var staffMembersDao: StaffMembersDaoImpl

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
