package com.bdev.hengschoolteacher.interactors.auth

import com.bdev.hengschoolteacher.dao.AuthDaoImpl
import com.bdev.hengschoolteacher.dao.AuthModel
import com.bdev.hengschoolteacher.data.auth.AuthInfo
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface AuthStorageInteractor {
    fun getAuthInfo(): AuthInfo?
    fun setAuthInfo(authInfo: AuthInfo)
    fun clearAuthInfo()
}

@EBean
open class AuthStorageInteractorImpl : AuthStorageInteractor {
    @Bean
    lateinit var authDao: AuthDaoImpl

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
