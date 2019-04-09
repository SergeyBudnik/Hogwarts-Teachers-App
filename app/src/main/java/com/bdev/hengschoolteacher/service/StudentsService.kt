package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.dao.StudentsDao
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student.StudentStatusType
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class StudentsService {
    @Bean
    lateinit var studentsDao: StudentsDao

    fun getAllStudents(): List<Student> {
        return studentsDao.getStudents()
    }

    fun getStudent(studentId: Long): Student? {
        return studentsDao.getStudents().find { it.id == studentId }
    }

    fun getGroupStudents(groupId: Long, weekIndex: Int): List<Student> {
        return studentsDao.getStudents().filter { student -> student
                .studentGroups
                .map { it.groupId }.contains(groupId) }
    }

    fun setStudents(students: List<Student>) {
        studentsDao.setStudents(students.filter { it.statusType == StudentStatusType.STUDYING })
    }
}
