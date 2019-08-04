package com.bdev.hengschoolteacher.ui.activities.settings

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.AuthService
import com.bdev.hengschoolteacher.service.profile.ProfileService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.LoginActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_settings.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@SuppressLint("Registered")
@EActivity(R.layout.activity_settings)
open class SettingsActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(SettingsActivity_::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Bean
    lateinit var authService: AuthService
    @Bean
    lateinit var profileService: ProfileService

    @AfterViews
    fun init() {
        settingsHeaderView.setLeftButtonAction { settingsMenuLayoutView.openMenu() }

        settingsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.SETTINGS)

        val me = profileService.getMe()

        settingsAccountNameView.text = me?.name ?: ""
        settingsAccountLoginView.text = me?.login ?: ""

        settingsLogoutView.setOnClickListener { logOut() }
    }

    private fun logOut() {
        authService.clearAuthInfo()

        LoginActivity.redirectToSibling(this)
    }
}
