package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.teacher.salary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringTeachersInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.interactors.teachers.TeacherSalaryInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import com.bdev.hengschoolteacher.ui.views.app.root.HtPageRootView
import com.bdev.hengschoolteacher.ui.views.app.monitoring.teacher.MonitoringTeacherHeaderView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_monitoring_teacher_salary.*
import javax.inject.Inject

@AndroidEntryPoint
class MonitoringTeacherSalaryPageFragment : BasePageFragment<MonitoringTeacherSalaryPageFragmentViewModel>() {
    companion object {
        const val EXTRA_TEACHER_LOGIN = "EXTRA_TEACHER_LOGIN"
    }

    lateinit var teacherLogin: String

    @Inject lateinit var staffMembersStorageInteractor: StaffMembersStorageInteractor
    @Inject lateinit var teacherSalaryInteractor: TeacherSalaryInteractor
    @Inject lateinit var alertsMonitoringTeachersService: AlertsMonitoringTeachersInteractor

    private var calendarEnabled = false

    override fun provideViewModel(): MonitoringTeacherSalaryPageFragmentViewModel =
        ViewModelProvider(this).get(MonitoringTeacherSalaryPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_monitoring_teacher_salary, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        // teacherLogin = intent.getStringExtra(EXTRA_TEACHER_LOGIN)!! todo

        initHeader()

        monitoringTeacherSalarySecondaryHeaderView.bind(
                currentItem = MonitoringTeacherHeaderView.Item.SALARY,
                teacherLogin = teacherLogin,
                hasLessonsAlert = alertsMonitoringTeachersService.haveLessonsAlerts(teacherLogin = teacherLogin),
                hasPaymentsAlert = alertsMonitoringTeachersService.havePaymentsAlerts(teacherLogin = teacherLogin),
                hasDebtsAlert = alertsMonitoringTeachersService.haveDebtsAlerts(teacherLogin = teacherLogin)
        )

        monitoringTeacherSalaryWeekSelectionBarView.init { weekIndex ->
            staffMembersStorageInteractor.getStaffMember(login = teacherLogin)?.let { teacher ->
                monitoringTeacherSalaryTeacherSalaryView.init(
                        teacher = teacher,
                        teacherPayments = teacherSalaryInteractor.getTeacherPayments(
                                teacherLogin = teacherLogin,
                                weekIndex = weekIndex
                        )
                )
            }
        }
    }

    private fun doFinish() {
//        finish()
//        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }

    private fun initHeader() {
        monitoringTeacherSalaryHeaderView
                .setLeftButtonAction { doFinish() }

        monitoringTeacherSalaryHeaderView.getFirstButtonHandler()
                .setAction(action = { toggleCalendar() })
                .setToggled(toggled = calendarEnabled)

        monitoringTeacherSalaryHeaderView.getSecondButtonHandler()
                .setAction(action = {
//                    TeacherPageFragment.redirectToChild(
//                            current = this,
//                            teacherLogin = teacherLogin
//                    )
                })
    }

    private fun toggleCalendar() {
        calendarEnabled = !calendarEnabled

        monitoringTeacherSalaryHeaderView.getFirstButtonHandler().setToggled(toggled = calendarEnabled)

        monitoringTeacherSalaryWeekSelectionBarView.visibility = visibleElseGone(visible = calendarEnabled)
    }
}
