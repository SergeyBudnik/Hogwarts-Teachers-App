package com.bdev.hengschoolteacher.ui.activities.monitoring.teacher

import android.os.Bundle
import android.os.PersistableBundle
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringTeachersInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.interactors.teachers.TeacherSalaryInteractor
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.teacher.TeacherActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.monitoring.teacher.MonitoringTeacherHeaderView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_monitoring_teacher_salary.*
import javax.inject.Inject

@AndroidEntryPoint
class MonitoringTeacherSalaryActivity : BaseActivity() {
    companion object {
        const val EXTRA_TEACHER_LOGIN = "EXTRA_TEACHER_LOGIN"

        fun redirectToSibling(current: BaseActivity, teacherLogin: String) {
            return RedirectBuilder
                    .redirect(current)
                    .to(MonitoringTeacherSalaryActivity::class.java)
                    .withExtra(EXTRA_TEACHER_LOGIN, teacherLogin)
                    .goAndCloseCurrent()
        }
    }

    lateinit var teacherLogin: String

    @Inject lateinit var staffMembersStorageInteractor: StaffMembersStorageInteractor
    @Inject lateinit var teacherSalaryInteractor: TeacherSalaryInteractor
    @Inject lateinit var alertsMonitoringTeachersService: AlertsMonitoringTeachersInteractor

    private var calendarEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_monitoring_teacher_salary)

        teacherLogin = intent.getStringExtra(EXTRA_TEACHER_LOGIN)!!

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

    override fun onBackPressed() {
        doFinish()
    }

    private fun doFinish() {
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }

    private fun initHeader() {
        monitoringTeacherSalaryHeaderView
                .setLeftButtonAction { doFinish() }

        monitoringTeacherSalaryHeaderView.getFirstButtonHandler()
                .setAction(action = { toggleCalendar() })
                .setToggled(toggled = calendarEnabled)

        monitoringTeacherSalaryHeaderView.getSecondButtonHandler()
                .setAction(action = {
                    TeacherActivity.redirectToChild(
                            current = this,
                            teacherLogin = teacherLogin
                    )
                })
    }

    private fun toggleCalendar() {
        calendarEnabled = !calendarEnabled

        monitoringTeacherSalaryHeaderView.getFirstButtonHandler().setToggled(toggled = calendarEnabled)

        monitoringTeacherSalaryWeekSelectionBarView.visibility = visibleElseGone(visible = calendarEnabled)
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
