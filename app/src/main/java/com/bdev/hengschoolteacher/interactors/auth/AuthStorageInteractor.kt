package com.bdev.hengschoolteacher.interactors.auth

import com.bdev.hengschoolteacher.dao.AuthDao
import com.bdev.hengschoolteacher.dao.AuthModel
import com.bdev.hengschoolteacher.data.auth.AuthInfo
import javax.inject.Inject

interface AuthStorageInteractor {
    fun getAuthInfo(): AuthInfo?
    fun setAuthInfo(authInfo: AuthInfo)
    fun clearAuthInfo()
}

class AuthStorageInteractorImpl @Inject constructor(
    private val authDao: AuthDao
): AuthStorageInteractor {
    override fun getAuthInfo(): AuthInfo? {
        return authDao.readValue().authInfo
    }

    override fun setAuthInfo(authInfo: AuthInfo) {
        authDao.writeValue(AuthModel(authInfo))
    }

    override fun clearAuthInfo() {
        authDao.writeValue(AuthModel(null))
    }
}
