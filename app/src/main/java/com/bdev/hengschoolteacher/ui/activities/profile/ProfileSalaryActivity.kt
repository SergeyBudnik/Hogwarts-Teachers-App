package com.bdev.hengschoolteacher.ui.activities.profile

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonStatusStorageInteractorImpl
import com.bdev.hengschoolteacher.interactors.UserPreferencesServiceImpl
import com.bdev.hengschoolteacher.interactors.alerts.profile.AlertsProfileInteractorImpl
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractorImpl
import com.bdev.hengschoolteacher.interactors.profile.ProfileServiceImpl
import com.bdev.hengschoolteacher.interactors.staff.StaffMembersStorageServiceImpl
import com.bdev.hengschoolteacher.interactors.teacher.TeacherSalaryServiceImpl
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
    lateinit var userPreferencesService: UserPreferencesServiceImpl
    @Bean
    lateinit var profileService: ProfileServiceImpl
    @Bean
    lateinit var lessonStatusService: LessonStatusStorageInteractorImpl
    @Bean
    lateinit var lessonsService: LessonsInteractorImpl
    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageServiceImpl
    @Bean
    lateinit var teacherSalaryService: TeacherSalaryServiceImpl
    @Bean
    lateinit var alertsProfileService: AlertsProfileInteractorImpl

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
