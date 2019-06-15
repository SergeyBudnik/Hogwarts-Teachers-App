package com.bdev.hengschoolteacher.ui.activities.settings

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.AuthService
import com.bdev.hengschoolteacher.service.UserPreferencesService
import com.bdev.hengschoolteacher.service.teacher.TeacherStorageService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.LoginActivity_
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_settings.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@SuppressLint("Registered")
@EActivity(R.layout.activity_settings)
open class SettingsActivity : BaseActivity() {
    @Bean
    lateinit var userPreferencesService: UserPreferencesService
    @Bean
    lateinit var teacherStorageService: TeacherStorageService
    @Bean
    lateinit var authService: AuthService

    @AfterViews
    fun init() {
        settingsHeaderView.setLeftButtonAction { settingsMenuLayoutView.openMenu() }

        settingsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.SETTINGS)

        val login = userPreferencesService.getUserLogin() ?: throw RuntimeException()
        val teacher = teacherStorageService.getTeacherByLogin(login) ?: throw RuntimeException()

        settingsAccountNameView.text = teacher.name
        settingsAccountLoginView.text = login

        settingsLogoutView.setOnClickListener { logOut() }
    }

    private fun logOut() {
        authService.clearAuthInfo()

        RedirectBuilder.redirect(this)
                .to(LoginActivity_::class.java)
                .goAndCloseCurrent()
    }
}
