package com.bdev.hengschoolteacher.ui.page_fragments.profile.salary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.alerts.profile.AlertsProfileInteractor
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonsStatusStorageInteractor
import com.bdev.hengschoolteacher.interactors.profile.ProfileInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.interactors.teachers.TeacherSalaryInteractor
import com.bdev.hengschoolteacher.interactors.user_preferences.UserPreferencesInteractor
import com.bdev.hengschoolteacher.ui.fragments.app_menu.AppMenuFragment
import com.bdev.hengschoolteacher.ui.fragments.app_menu.data.AppMenuItem
import com.bdev.hengschoolteacher.ui.fragments.profile.header.ProfileHeaderFragment
import com.bdev.hengschoolteacher.ui.fragments.profile.header.data.ProfileHeaderFragmentItem
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_profile_salary.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileSalaryPageFragment : BasePageFragment<ProfileSalaryPageFragmentViewModel>() {
    @Inject lateinit var userPreferencesInteractor: UserPreferencesInteractor
    @Inject lateinit var profileInteractor: ProfileInteractor
    @Inject lateinit var lessonsStatusService: LessonsStatusStorageInteractor
    @Inject lateinit var lessonsService: LessonsInteractor
    @Inject lateinit var staffMembersStorageInteractor: StaffMembersStorageInteractor
    @Inject lateinit var teacherSalaryInteractor: TeacherSalaryInteractor
    @Inject lateinit var alertsProfileService: AlertsProfileInteractor

    private var calendarEnabled = false

    override fun provideViewModel(): ProfileSalaryPageFragmentViewModel =
        ViewModelProvider(this).get(ProfileSalaryPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_profile_salary, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        initHeader()

        getMenu().setCurrentItem(item = AppMenuItem.MY_PROFILE)
        getSecondaryHeaderFragment().setCurrentItem(item = ProfileHeaderFragmentItem.SALARY)

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

    private fun getSecondaryHeaderFragment(): ProfileHeaderFragment =
        childFragmentManager.findFragmentById(R.id.profileSalarySecondaryHeaderFragment) as ProfileHeaderFragment

    private fun getMenu(): AppMenuFragment =
        childFragmentManager.findFragmentById(R.id.appMenuFragment) as AppMenuFragment
}
