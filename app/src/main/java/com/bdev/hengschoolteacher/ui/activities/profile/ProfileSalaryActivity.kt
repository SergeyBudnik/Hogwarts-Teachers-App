package com.bdev.hengschoolteacher.ui.activities.profile

import android.os.Bundle
import android.os.PersistableBundle
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.user_preferences.UserPreferencesInteractor
import com.bdev.hengschoolteacher.interactors.alerts.profile.AlertsProfileInteractor
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonsStatusStorageInteractor
import com.bdev.hengschoolteacher.interactors.profile.ProfileInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.interactors.teachers.TeacherSalaryInteractor
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import com.bdev.hengschoolteacher.ui.views.app.profile.ProfileHeaderView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_profile_salary.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileSalaryActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(ProfileSalaryActivity::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Inject lateinit var userPreferencesInteractor: UserPreferencesInteractor
    @Inject lateinit var profileInteractor: ProfileInteractor
    @Inject lateinit var lessonsStatusService: LessonsStatusStorageInteractor
    @Inject lateinit var lessonsService: LessonsInteractor
    @Inject lateinit var staffMembersStorageInteractor: StaffMembersStorageInteractor
    @Inject lateinit var teacherSalaryInteractor: TeacherSalaryInteractor
    @Inject lateinit var alertsProfileService: AlertsProfileInteractor

    private var calendarEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile_salary)

        initHeader()

        profileSalarySecondaryHeaderView.bind(
                ProfileHeaderView.Item.SALARY,
                hasLessonsAlert = alertsProfileService.haveLessonsAlerts(),
                hasDebtsAlert = alertsProfileService.haveDebtsAlerts(),
                hasPaymentsAlert = alertsProfileService.havePaymentsAlerts()
        )

        profileSalaryMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MY_PROFILE)

        val me = profileInteractor.getMe()

        if (me != null) {
            profileSalaryWeekSelectionBarView.init { weekIndex ->
                staffMembersStorageInteractor.getStaffMember(login = me.login)?.let { teacher ->
                    profileSalaryTeacherSalaryView.init(
                            teacher = teacher,
                            teacherPayments = teacherSalaryInteractor.getTeacherPayments(
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
