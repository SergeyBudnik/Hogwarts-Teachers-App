package com.bdev.hengschoolteacher.interactors.teachers

import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import javax.inject.Inject

interface TeacherInfoInteractor {
    fun getTeachersName(staffMember: StaffMember): String
    fun getTeachersSurname(staffMember: StaffMember): String
}

class TeacherInfoInteractorImpl @Inject constructor(): TeacherInfoInteractor {
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
