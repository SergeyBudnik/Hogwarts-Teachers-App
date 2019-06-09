package com.bdev.hengschoolteacher.service.teacher

import com.bdev.hengschoolteacher.data.school.teacher.Teacher
import org.androidannotations.annotations.EBean

@EBean
open class TeacherInfoService {
    fun getTeachersName(teacher: Teacher): String {
        val teacherNameAndSurname = teacher.name.split(" ")

        return if (teacherNameAndSurname.isEmpty()) {
            ""
        } else {
            teacherNameAndSurname[0]
        }
    }

    fun getTeachersSurname(teacher: Teacher): String {
        val teacherNameAndSurname = teacher.name.split(" ")

        return if (teacherNameAndSurname.size < 2) {
            ""
        } else {
            teacherNameAndSurname[1]
        }
    }
}
