package com.bdev.hengschoolteacher.interactors.user_preferences

import com.bdev.hengschoolteacher.dao.UserPreferencesDao
import com.bdev.hengschoolteacher.dao.UserPreferencesModel
import javax.inject.Inject

interface UserPreferencesInteractor {
    fun getUserLogin(): String?
    fun getUserPassword(): String?
    fun setUserCredentials(login: String, password: String)
}

class UserPreferencesInteractorImpl @Inject constructor(
    private val userPreferencesDao: UserPreferencesDao
): UserPreferencesInteractor {
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
