package com.bdev.hengschoolteacher.ui.activities

import android.os.Bundle
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractor
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartActivity : BaseActivity() {
    @Inject lateinit var authService: AuthStorageInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authInfo = authService.getAuthInfo()

        // appUpdateService.enqueueUpdate()

        if (authInfo == null) {
            LoginActivity.redirectToSibling(this)
        } else {
            LoadingActivity.redirectToSibling(this)
        }
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
