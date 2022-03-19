package com.bdev.hengschoolteacher.interactors.teacher

import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import org.androidannotations.annotations.EBean

interface TeacherInfoService {
    fun getTeachersName(staffMember: StaffMember): String
    fun getTeachersSurname(staffMember: StaffMember): String
}

@EBean
open class TeacherInfoServiceImpl : TeacherInfoService {
    override fun getTeachersName(staffMember: StaffMember): String {
        val teacherNameAndSurname = staffMember.person.name.split(" ")

        return if (teacherNameAndSurname.isEmpty()) {
            ""
        } else {
            teacherNameAndSurname[0]
        }
    }

    override fun getTeachersSurname(staffMember: StaffMember): String {
        val teacherNameAndSurname = staffMember.person.name.split(" ")

        return if (teacherNameAndSurname.size < 2) {
            ""
        } else {
            teacherNameAndSurname[1]
        }
    }
}
