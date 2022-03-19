package com.bdev.hengschoolteacher.ui.activities.settings

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractorImpl
import com.bdev.hengschoolteacher.interactors.profile.ProfileServiceImpl
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.LoginActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
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
    lateinit var authService: AuthStorageInteractorImpl
    @Bean
    lateinit var profileService: ProfileServiceImpl

    @AfterViews
    fun init() {
        settingsHeaderView.setLeftButtonAction { settingsMenuLayoutView.openMenu() }

        settingsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.SETTINGS)

        val me = profileService.getMe()

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
