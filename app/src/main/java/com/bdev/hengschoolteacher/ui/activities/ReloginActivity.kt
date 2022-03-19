package com.bdev.hengschoolteacher.ui.activities

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.Animation
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.auth.AuthActionsInteractorImpl
import com.bdev.hengschoolteacher.data.auth.AuthCredentials
import com.bdev.hengschoolteacher.interactors.UserPreferencesServiceImpl
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import kotlinx.android.synthetic.main.activity_relogin.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.res.AnimationRes

@SuppressLint("Registered")
@EActivity(R.layout.activity_relogin)
open class ReloginActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(ReloginActivity_::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Bean
    lateinit var authAsyncService: AuthActionsInteractorImpl
    @Bean
    lateinit var userPreferencesService: UserPreferencesServiceImpl

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
        LoadingActivity.redirectToSibling(this)
    }

    private fun onReloginFailure() {
        LoginActivity.redirectToSibling(this)
    }

    private fun onOtherFailure() {
        reloginInProgressSpinnerView.clearAnimation()
        reloginInProgressView.visibility = View.GONE

        reloginFailedView.visibility = View.VISIBLE
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
