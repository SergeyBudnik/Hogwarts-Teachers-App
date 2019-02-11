package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.dao.UserPreferencesDao
import com.bdev.hengschoolteacher.dao.UserPreferencesModel
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class UserPreferencesService {
    @Bean
    lateinit var userPreferencesDao: UserPreferencesDao

    fun getUserLogin(): String? {
        return userPreferencesDao.getUserPreferences().login
    }

    fun getUserPassword(): String? {
        return userPreferencesDao.getUserPreferences().password
    }

    fun setUserCredentials(login: String, password: String) {
        userPreferencesDao.setUserPreferences(UserPreferencesModel(login, password))
    }

    fun clearUserPreferences() {
        userPreferencesDao.setUserPreferences(UserPreferencesModel(null, null))
    }
}
