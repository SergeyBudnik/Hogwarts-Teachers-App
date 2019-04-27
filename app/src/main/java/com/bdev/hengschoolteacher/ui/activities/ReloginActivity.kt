package com.bdev.hengschoolteacher.ui.activities

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.Animation
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.async.AuthAsyncService
import com.bdev.hengschoolteacher.data.auth.AuthCredentials
import com.bdev.hengschoolteacher.service.UserPreferencesService
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder.Companion.redirect
import kotlinx.android.synthetic.main.activity_relogin.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.res.AnimationRes

@SuppressLint("Registered")
@EActivity(R.layout.activity_relogin)
open class ReloginActivity : BaseActivity() {
    @Bean
    lateinit var authAsyncService: AuthAsyncService
    @Bean
    lateinit var userPreferencesService: UserPreferencesService

    @AnimationRes(R.anim.spinner)
    lateinit var spinnerAnim: Animation

    @AfterViews
    fun init() {
        doRelogin()

        reloginFailedRestartView.setOnClickListener { doRelogin() }
    }

    private fun doRelogin() {
        reloginInProgressView.visibility = View.VISIBLE
        reloginInProgressSpinnerView.startAnimation(spinnerAnim)

        reloginFailedView.visibility = View.GONE

        val login = userPreferencesService.getUserLogin()
        val password = userPreferencesService.getUserPassword()

        if (login == null || password == null) {
            onReloginFailure()
        } else {
            authAsyncService.login(AuthCredentials(login, password))
                    .onSuccess { runOnUiThread { onReloginSuccess() } }
                    .onAuthFail { runOnUiThread { onReloginFailure() } }
                    .onOtherFail { runOnUiThread { onOtherFailure() } }
        }
    }

    private fun onReloginSuccess() {
        redirect(this).to(LoadingActivity_::class.java).goAndCloseCurrent()
    }

    private fun onReloginFailure() {
        redirect(this).to(LoginActivity_::class.java).go()
    }

    private fun onOtherFailure() {
        reloginInProgressSpinnerView.clearAnimation()
        reloginInProgressView.visibility = View.GONE

        reloginFailedView.visibility = View.VISIBLE
    }
}
