package com.bdev.hengschoolteacher.ui.activities.profile

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.services.LessonStatusService
import com.bdev.hengschoolteacher.services.UserPreferencesService
import com.bdev.hengschoolteacher.services.alerts.profile.AlertsProfileService
import com.bdev.hengschoolteacher.services.lessons.LessonsService
import com.bdev.hengschoolteacher.services.profile.ProfileService
import com.bdev.hengschoolteacher.services.staff.StaffMembersStorageService
import com.bdev.hengschoolteacher.services.teacher.TeacherSalaryService
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
    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageService
    @Bean
    lateinit var teacherSalaryService: TeacherSalaryService
    @Bean
    lateinit var alertsProfileService: AlertsProfileService

    private var calendarEnabled = false

    @AfterViews
    fun init() {
        initHeader()

        profileSalarySecondaryHeaderView.bind(
                ProfileHeaderView.Item.SALARY,
                hasLessonsAlert = alertsProfileService.haveLessonsAlerts(),
                hasDebtsAlert = alertsProfileService.haveDebtsAlerts(),
                hasPaymentsAlert = alertsProfileService.havePaymentsAlerts()
        )

        profileSalaryMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MY_PROFILE)

        val me = profileService.getMe()

        if (me != null) {
            profileSalaryWeekSelectionBarView.init { weekIndex ->
                staffMembersStorageService.getStaffMember(login = me.login)?.let { teacher ->
                    profileSalaryTeacherSalaryView.init(
                            teacher = teacher,
                            teacherPayments = teacherSalaryService.getTeacherPayments(
                                    teacherLogin = me.login,
                                    weekIndex = weekIndex
                            )
                    )
                }


            }
        }
    }

    private fun initHeader() {
        profileSalaryHeaderView
                .setLeftButtonAction { profileSalaryMenuLayoutView.openMenu() }

        profileSalaryHeaderView.getFirstButtonHandler()
                .setAction(action = { toggleCalendar() })
                .setToggled(toggled = calendarEnabled)
    }

    private fun toggleCalendar() {
        calendarEnabled = !calendarEnabled

        profileSalaryHeaderView.getFirstButtonHandler().setToggled(toggled = calendarEnabled)

        profileSalaryWeekSelectionBarView.visibility = visibleElseGone(visible = calendarEnabled)
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
