package com.bdev.hengschoolteacher.interactors.students

import com.bdev.hengschoolteacher.dao.StudentsDaoImpl
import com.bdev.hengschoolteacher.dao.StudentsModel
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student.StudentStatusType
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface StudentsStorageInteractor {
    fun getAll(): List<Student>
    fun getByLogin(studentLogin: String): Student?
    fun setAll(students: List<Student>)
}

@EBean
open class StudentsStorageInteractorImpl : StudentsStorageInteractor {
    @Bean
    lateinit var studentsDao: StudentsDaoImpl

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
