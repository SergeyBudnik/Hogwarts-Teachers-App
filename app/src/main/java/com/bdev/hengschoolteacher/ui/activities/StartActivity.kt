package com.bdev.hengschoolteacher.ui.activities

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractorImpl
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import org.androidannotations.annotations.AfterInject
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@SuppressLint("Registered")
@EActivity
open class StartActivity : BaseActivity() {
    @Bean
    lateinit var authService: AuthStorageInteractorImpl

    @AfterInject
    fun init() {
        val authInfo = authService.getAuthInfo()

        appUpdateService.enqueueUpdate()

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
