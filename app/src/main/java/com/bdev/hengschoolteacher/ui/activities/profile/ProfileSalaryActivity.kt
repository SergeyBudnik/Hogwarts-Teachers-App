package com.bdev.hengschoolteacher.ui.activities.profile

import android.annotation.SuppressLint
import android.view.View
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.LessonStatusService
import com.bdev.hengschoolteacher.service.LessonsService
import com.bdev.hengschoolteacher.service.teacher.TeacherStorageService
import com.bdev.hengschoolteacher.service.UserPreferencesService
import com.bdev.hengschoolteacher.service.profile.ProfileService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.HeaderElementsUtils
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
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
    lateinit var teacherStorageService: TeacherStorageService
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
                .setFirstRightButtonColor(getHeaderButtonColor(calendarEnabled))

        profileSalaryMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MY_PROFILE)

        val me = profileService.getMe()

        if (me != null) {
            profileSalaryWeekSelectionBarView.init { weekIndex ->
                profileSalaryTeacherSalaryView.init(me.id, weekIndex)
            }
        }
    }

    private fun toggleCalendar() {
        calendarEnabled = !calendarEnabled

        profileSalaryHeaderView.setFirstRightButtonColor(getHeaderButtonColor(calendarEnabled))

        profileSalaryWeekSelectionBarView.visibility = if (calendarEnabled) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun getHeaderButtonColor(enabled: Boolean): Int {
        return HeaderElementsUtils.getColor(this, enabled)
    }
}
