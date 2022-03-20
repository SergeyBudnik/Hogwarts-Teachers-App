package com.bdev.hengschoolteacher.ui.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.auth.AuthCredentials
import com.bdev.hengschoolteacher.interactors.auth.AuthActionsInteractor
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(LoginActivity::class.java)
                    .goAndCloseAll()
        }
    }

    @Inject lateinit var authAsyncService: AuthActionsInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

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
