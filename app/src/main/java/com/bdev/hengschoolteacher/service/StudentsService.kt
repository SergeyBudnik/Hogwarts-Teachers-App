package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.dao.StudentsDao
import com.bdev.hengschoolteacher.dao.StudentsModel
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student.StudentStatusType
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class StudentsService {
    @Bean
    lateinit var studentsDao: StudentsDao

    fun getAllStudents(): List<Student> {
        return studentsDao.readValue().students
    }

    fun getStudent(studentLogin: String): Student? {
        return studentsDao.readValue().students.find { it.login == studentLogin }
    }

    fun getGroupStudents(groupId: Long): List<Student> {
        return studentsDao
                .readValue()
                .students
                .filter { student -> student.studentGroups.map { it.groupId }.contains(groupId) }
    }

    fun setStudents(students: List<Student>) {
        studentsDao.writeValue(StudentsModel(
                students.filter { it.statusType == StudentStatusType.STUDYING })
        )
    }
}
