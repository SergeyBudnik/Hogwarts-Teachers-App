package com.bdev.hengschoolteacher.ui.activities.settings

import android.os.Bundle
import android.os.PersistableBundle
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractor
import com.bdev.hengschoolteacher.interactors.profile.ProfileInteractor
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.LoginActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_settings.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(SettingsActivity::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Inject lateinit var authService: AuthStorageInteractor
    @Inject lateinit var profileInteractor: ProfileInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        settingsHeaderView.setLeftButtonAction { settingsMenuLayoutView.openMenu() }

        settingsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.SETTINGS)

        val me = profileInteractor.getMe()

        settingsAccountNameView.text = me?.person?.name ?: ""
        settingsAccountLoginView.text = me?.login ?: ""

        settingsLogoutView.setOnClickListener { logOut() }
    }

    private fun logOut() {
        authService.clearAuthInfo()

        LoginActivity.redirectToSibling(this)
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
