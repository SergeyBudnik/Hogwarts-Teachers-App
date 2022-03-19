package com.bdev.hengschoolteacher.interactors

import com.bdev.hengschoolteacher.dao.UserPreferencesDaoImpl
import com.bdev.hengschoolteacher.dao.UserPreferencesModel
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface UserPreferencesService {
    fun getUserLogin(): String?
    fun getUserPassword(): String?
    fun setUserCredentials(login: String, password: String)
}

@EBean
open class UserPreferencesServiceImpl : UserPreferencesService {
    @Bean
    lateinit var userPreferencesDao: UserPreferencesDaoImpl

    override fun getUserLogin(): String? {
        return userPreferencesDao.readValue().login
    }

    override fun getUserPassword(): String? {
        return userPreferencesDao.readValue().password
    }

    override fun setUserCredentials(login: String, password: String) {
        userPreferencesDao.writeValue(UserPreferencesModel(login, password))
    }
}
