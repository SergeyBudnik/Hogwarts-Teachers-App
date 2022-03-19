package com.bdev.hengschoolteacher.interactors.auth

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.data.auth.AuthCredentials
import com.bdev.hengschoolteacher.data.auth.AuthInfo
import com.bdev.hengschoolteacher.interactors.UserPreferencesServiceImpl
import com.bdev.hengschoolteacher.network.api.auth.AuthApiProviderImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface AuthActionsInteractor {
    fun login(authCredentials: AuthCredentials): SmartPromise<AuthInfo, Exception>
}

@EBean
open class AuthActionsInteractorImpl : AuthActionsInteractor {
    @Bean
    lateinit var authApiProvider: AuthApiProviderImpl

    @Bean
    lateinit var authService: AuthStorageInteractorImpl
    @Bean
    lateinit var userPreferencesService: UserPreferencesServiceImpl

    override fun login(authCredentials: AuthCredentials): SmartPromise<AuthInfo, Exception> {
        return smartTask {
            val authInfo = authApiProvider.provide().login(authCredentials).execute().body()!!

            authService.setAuthInfo(authInfo)

            userPreferencesService.setUserCredentials(
                    authCredentials.login,
                    authCredentials.password
            )

            return@smartTask authInfo
        }
    }
}
