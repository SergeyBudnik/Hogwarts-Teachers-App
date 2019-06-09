package com.bdev.hengschoolteacher.ui.activities.profile

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.AuthService
import com.bdev.hengschoolteacher.service.teacher.TeacherStorageService
import com.bdev.hengschoolteacher.service.UserPreferencesService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.LoginActivity_
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder.Companion.redirect
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_profile_account.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@SuppressLint("Registered")
@EActivity(R.layout.activity_profile_account)
open class ProfileAccountActivity : BaseActivity() {
    @Bean
    lateinit var userPreferencesService: UserPreferencesService
    @Bean
    lateinit var teacherStorageService: TeacherStorageService
    @Bean
    lateinit var authService: AuthService

    @AfterViews
    fun init() {
        profileAccountHeaderView.setLeftButtonAction { profileAccountMenuLayoutView.openMenu() }

        profileAccountMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MY_PROFILE)

        val login = userPreferencesService.getUserLogin() ?: throw RuntimeException()
        val teacher = teacherStorageService.getTeacherByLogin(login) ?: throw RuntimeException()

        profileAccountNameView.text = teacher.name
        profileAccountLoginView.text = login

        profileAccountLogoutView.setOnClickListener { logOut() }
    }

    private fun logOut() {
        authService.clearAuthInfo()

        redirect(this)
                .to(LoginActivity_::class.java)
                .goAndCloseCurrent()
    }
}
