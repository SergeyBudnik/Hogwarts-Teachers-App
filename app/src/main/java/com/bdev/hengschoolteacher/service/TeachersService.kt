package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.dao.TeachersDao
import com.bdev.hengschoolteacher.data.school.teacher.Teacher
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class TeachersService {
    @Bean
    lateinit var teachersDao: TeachersDao
    @Bean
    lateinit var userPreferencesService: UserPreferencesService

    fun getAllTeachers(): List<Teacher> {
        return teachersDao.getTeachers()
    }

    fun getTeacherMe(): Teacher {
        val login = userPreferencesService.getUserLogin() ?: throw RuntimeException()

        return getTeacherByLogin(login) ?: throw RuntimeException()
    }

    fun getTeacherById(id: Long): Teacher? {
        return teachersDao.getTeachers().find { it.id == id }
    }

    fun getTeacherByLogin(login: String): Teacher? {
        return teachersDao.getTeachers().find { it.login == login }
    }

    fun setTeachers(teachers: List<Teacher>) {
        teachersDao.setTeachers(teachers)
    }
}
