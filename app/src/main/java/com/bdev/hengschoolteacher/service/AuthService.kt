package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.dao.AuthDao
import com.bdev.hengschoolteacher.data.auth.AuthInfo
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class AuthService {
    @Bean
    lateinit var authDao: AuthDao

    fun getAuthInfo(): AuthInfo? {
        return authDao.getAuthInfo()
    }

    fun setAuthInfo(authInfo: AuthInfo) {
        authDao.setAuthInfo(authInfo)
    }

    fun clearAuthInfo() {
        authDao.setAuthInfo(null)
    }
}
