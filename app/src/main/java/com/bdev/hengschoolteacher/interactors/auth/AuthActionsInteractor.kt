package com.bdev.hengschoolteacher.interactors.auth

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.data.auth.AuthCredentials
import com.bdev.hengschoolteacher.data.auth.AuthInfo
import com.bdev.hengschoolteacher.interactors.user_preferences.UserPreferencesInteractor
import com.bdev.hengschoolteacher.network.api.auth.AuthApiProvider
import javax.inject.Inject

interface AuthActionsInteractor {
    fun login(authCredentials: AuthCredentials): SmartPromise<AuthInfo, Exception>
}

class AuthActionsInteractorImpl @Inject constructor(
    private val authApiProvider: AuthApiProvider,
    private val authService: AuthStorageInteractor,
    private val userPreferencesInteractor: UserPreferencesInteractor
): AuthActionsInteractor {
    override fun login(authCredentials: AuthCredentials): SmartPromise<AuthInfo, Exception> {
        return smartTask {
            val authInfo = authApiProvider.provide().login(authCredentials).execute().body()!!

            authService.setAuthInfo(authInfo)

            userPreferencesInteractor.setUserCredentials(
                    authCredentials.login,
                    authCredentials.password
            )

            return@smartTask authInfo
        }
    }
}
