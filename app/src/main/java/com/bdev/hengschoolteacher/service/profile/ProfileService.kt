package com.bdev.hengschoolteacher.service.profile

import com.bdev.hengschoolteacher.data.school.teacher.Teacher
import com.bdev.hengschoolteacher.service.UserPreferencesService
import com.bdev.hengschoolteacher.service.teacher.TeacherStorageService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class ProfileService {
    @Bean
    lateinit var userPreferencesService: UserPreferencesService
    @Bean
    lateinit var teacherStorageService: TeacherStorageService

    fun getMe(): Teacher? {
        val login = userPreferencesService.getUserLogin()

        return if (login != null) {
            teacherStorageService.getTeacherByLogin(login)
        } else {
            null
        }
    }
}
