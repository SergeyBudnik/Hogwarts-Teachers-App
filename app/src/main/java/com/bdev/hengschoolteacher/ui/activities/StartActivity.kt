package com.bdev.hengschoolteacher.ui.activities

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.service.AuthService
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder.Companion.redirect
import org.androidannotations.annotations.AfterInject
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@SuppressLint("Registered")
@EActivity
open class StartActivity : BaseActivity() {
    @Bean
    lateinit var authService: AuthService

    @AfterInject
    fun init() {
        val authInfo = authService.getAuthInfo()

        if (authInfo == null) {
            redirect(this).to(LoginActivity_::class.java).goAndCloseCurrent()
        } else {
            redirect(this).to(LoadingActivity_::class.java).goAndCloseCurrent()
        }
    }
}
