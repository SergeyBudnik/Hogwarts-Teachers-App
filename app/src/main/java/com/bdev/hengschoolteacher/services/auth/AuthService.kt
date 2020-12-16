package com.bdev.hengschoolteacher.services.auth

import com.bdev.hengschoolteacher.dao.AuthDao
import com.bdev.hengschoolteacher.dao.AuthModel
import com.bdev.hengschoolteacher.data.auth.AuthInfo
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class AuthService {
    @Bean
    lateinit var authDao: AuthDao

    fun getAuthInfo(): AuthInfo? {
        return authDao.readValue().authInfo
    }

    fun setAuthInfo(authInfo: AuthInfo) {
        authDao.writeValue(AuthModel(authInfo))
    }

    fun clearAuthInfo() {
        authDao.writeValue(AuthModel(null))
    }
}
