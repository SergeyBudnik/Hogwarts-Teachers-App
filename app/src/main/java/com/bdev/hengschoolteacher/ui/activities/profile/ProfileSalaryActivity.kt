package com.bdev.hengschoolteacher.ui.activities.profile

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.services.LessonStatusService
import com.bdev.hengschoolteacher.services.lessons.LessonsService
import com.bdev.hengschoolteacher.services.UserPreferencesService
import com.bdev.hengschoolteacher.services.profile.ProfileService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import com.bdev.hengschoolteacher.ui.views.app.profile.ProfileHeaderView
import kotlinx.android.synthetic.main.activity_profile_salary.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@SuppressLint("Registered")
@EActivity(R.layout.activity_profile_salary)
open class ProfileSalaryActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(ProfileSalaryActivity_::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Bean
    lateinit var userPreferencesService: UserPreferencesService
    @Bean
    lateinit var profileService: ProfileService
    @Bean
    lateinit var lessonStatusService: LessonStatusService
    @Bean
    lateinit var lessonsService: LessonsService

    private var calendarEnabled = false

    @AfterViews
    fun init() {
        profileSalaryHeaderView
                .setLeftButtonAction { profileSalaryMenuLayoutView.openMenu() }
                .setFirstRightButtonAction { toggleCalendar() }
                .setFirstRightButtonActive(calendarEnabled)

        profileSalarySecondaryHeaderView.bind(ProfileHeaderView.Item.SALARY)

        profileSalaryMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MY_PROFILE)

        val me = profileService.getMe()

        if (me != null) {
            profileSalaryWeekSelectionBarView.init { weekIndex ->
                profileSalaryTeacherSalaryView.init(me.login, weekIndex)
            }
        }
    }

    private fun toggleCalendar() {
        calendarEnabled = !calendarEnabled

        profileSalaryHeaderView.setFirstRightButtonActive(calendarEnabled)

        profileSalaryWeekSelectionBarView.visibility = visibleElseGone(visible = calendarEnabled)
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
