package com.bdev.hengschoolteacher.services

import com.bdev.hengschoolteacher.dao.UserPreferencesDao
import com.bdev.hengschoolteacher.dao.UserPreferencesModel
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class UserPreferencesService {
    @Bean
    lateinit var userPreferencesDao: UserPreferencesDao

    fun getUserLogin(): String? {
        return userPreferencesDao.readValue().login
    }

    fun getUserPassword(): String? {
        return userPreferencesDao.readValue().password
    }

    fun setUserCredentials(login: String, password: String) {
        userPreferencesDao.writeValue(UserPreferencesModel(login, password))
    }
}
