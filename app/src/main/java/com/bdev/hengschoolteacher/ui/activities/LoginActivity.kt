package com.bdev.hengschoolteacher.ui.activities

import android.annotation.SuppressLint
import android.widget.Toast
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.async.AuthAsyncService
import com.bdev.hengschoolteacher.data.auth.AuthCredentials
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import kotlinx.android.synthetic.main.activity_login.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@SuppressLint("Registered")
@EActivity(R.layout.activity_login)
open class LoginActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(LoginActivity_::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Bean
    lateinit var authAsyncService: AuthAsyncService

    @AfterViews
    fun init() {
        loginDoLoginView.setOnClickListener { doLogin() }
    }

    private fun doLogin() {
        val login = loginLoginView.getText()
        val password = loginPasswordView.getText()

        authAsyncService.login(AuthCredentials(login, password))
                .onSuccess { runOnUiThread { onLoginSuccessful() } }
                .onAuthFail { runOnUiThread { onLoginFailure() } }
                .onOtherFail { runOnUiThread { onLoginFailure() } }
    }

    private fun onLoginSuccessful() {
        LoadingActivity.redirectToSibling(this)
    }

    private fun onLoginFailure() {
        Toast.makeText(this, "FAIL", Toast.LENGTH_SHORT).show()
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
