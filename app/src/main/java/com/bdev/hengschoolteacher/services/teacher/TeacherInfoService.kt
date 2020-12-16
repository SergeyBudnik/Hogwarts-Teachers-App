package com.bdev.hengschoolteacher.services.teacher

import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import org.androidannotations.annotations.EBean

@EBean
open class TeacherInfoService {
    fun getTeachersName(staffMember: StaffMember): String {
        val teacherNameAndSurname = staffMember.person.name.split(" ")

        return if (teacherNameAndSurname.isEmpty()) {
            ""
        } else {
            teacherNameAndSurname[0]
        }
    }

    fun getTeachersSurname(staffMember: StaffMember): String {
        val teacherNameAndSurname = staffMember.person.name.split(" ")

        return if (teacherNameAndSurname.size < 2) {
            ""
        } else {
            teacherNameAndSurname[1]
        }
    }
}
