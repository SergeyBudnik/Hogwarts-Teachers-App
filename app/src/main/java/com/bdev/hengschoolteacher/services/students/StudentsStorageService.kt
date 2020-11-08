package com.bdev.hengschoolteacher.services.students

import com.bdev.hengschoolteacher.dao.StudentsDao
import com.bdev.hengschoolteacher.dao.StudentsModel
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student.StudentStatusType
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface StudentsStorageService {
    fun getAll(): List<Student>
    fun getByLogin(studentLogin: String): Student?
    fun setAll(students: List<Student>)
}

@EBean
open class StudentsStorageServiceImpl : StudentsStorageService {
    @Bean
    lateinit var studentsDao: StudentsDao

    override fun getAll(): List<Student> {
        return studentsDao.readValue().students
    }

    override fun getByLogin(studentLogin: String): Student? {
        return studentsDao.readValue().students.find { it.login == studentLogin }
    }

    override fun setAll(students: List<Student>) {
        studentsDao.writeValue(StudentsModel(
                students.filter { it.statusType == StudentStatusType.STUDYING })
        )
    }
}
