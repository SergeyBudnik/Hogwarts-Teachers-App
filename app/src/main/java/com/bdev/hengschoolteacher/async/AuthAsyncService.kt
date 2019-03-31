package com.bdev.hengschoolteacher.async

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.data.auth.AuthCredentials
import com.bdev.hengschoolteacher.data.auth.AuthInfo
import com.bdev.hengschoolteacher.rest.AuthRest
import com.bdev.hengschoolteacher.service.AuthService
import com.bdev.hengschoolteacher.service.UserPreferencesService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.rest.spring.annotations.RestService

@EBean
open class AuthAsyncService : CommonAsyncService() {
    @RestService
    lateinit var authRest: AuthRest

    @Bean
    lateinit var authService: AuthService
    @Bean
    lateinit var userPreferencesService: UserPreferencesService

    fun login(authCredentials: AuthCredentials): SmartPromise<AuthInfo, Exception> {
        return smartTask {
            val authInfo = trustSsl(authRest).login(authCredentials)

            authService.setAuthInfo(authInfo)

            userPreferencesService.setUserCredentials(
                    authCredentials.login,
                    authCredentials.password
            )

            return@smartTask authInfo
        }
    }
}
