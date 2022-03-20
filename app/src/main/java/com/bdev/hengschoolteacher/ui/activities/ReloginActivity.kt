package com.bdev.hengschoolteacher.ui.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.animation.AnimationUtils
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.auth.AuthCredentials
import com.bdev.hengschoolteacher.interactors.auth.AuthActionsInteractorImpl
import com.bdev.hengschoolteacher.interactors.user_preferences.UserPreferencesInteractor
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_relogin.*
import javax.inject.Inject

@AndroidEntryPoint
class ReloginActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(ReloginActivity::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Inject lateinit var authAsyncService: AuthActionsInteractorImpl
    @Inject lateinit var userPreferencesService: UserPreferencesInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_relogin)

        doRelogin()

        reloginFailedRestartView.setOnClickListener { doRelogin() }
    }

    private fun doRelogin() {
        val spinnerAnim = AnimationUtils.loadAnimation(this, R.anim.spinner)

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
