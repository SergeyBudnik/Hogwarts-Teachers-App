package com.bdev.hengschoolteacher.interactors.auth

import com.bdev.hengschoolteacher.async.CommonAsyncService
import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.data.auth.AuthCredentials
import com.bdev.hengschoolteacher.data.auth.AuthInfo
import com.bdev.hengschoolteacher.rest.AuthRest
import com.bdev.hengschoolteacher.interactors.UserPreferencesServiceImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.rest.spring.annotations.RestService

interface AuthActionsInteractor {
    fun login(authCredentials: AuthCredentials): SmartPromise<AuthInfo, Exception>
}

@EBean
open class AuthActionsInteractorImpl : AuthActionsInteractor, CommonAsyncService() {
    @RestService
    lateinit var authRest: AuthRest

    @Bean
    lateinit var authService: AuthStorageInteractorImpl
    @Bean
    lateinit var userPreferencesService: UserPreferencesServiceImpl

    override fun login(authCredentials: AuthCredentials): SmartPromise<AuthInfo, Exception> {
        return smartTask {
            val authInfo = authRest.login(authCredentials)

            authService.setAuthInfo(authInfo)

            userPreferencesService.setUserCredentials(
                    authCredentials.login,
                    authCredentials.password
            )

            return@smartTask authInfo
        }
    }
}
