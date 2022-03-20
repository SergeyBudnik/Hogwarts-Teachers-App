package com.bdev.hengschoolteacher.ui.activities.monitoring.teacher

import android.os.Bundle
import android.os.PersistableBundle
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringTeachersInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_payments.StudentsPaymentsActionsInteractor
import com.bdev.hengschoolteacher.interactors.students_payments.StudentsPaymentsProviderInteractor
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.teacher.TeacherActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.monitoring.teacher.MonitoringTeacherHeaderView
import com.bdev.hengschoolteacher.ui.views.app.payments.PaymentsItemViewData
import com.bdev.hengschoolteacher.ui.views.app.payments.PaymentsViewData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_monitoring_teacher_payments.*
import javax.inject.Inject

@AndroidEntryPoint
class MonitoringTeacherPaymentsActivity : BaseActivity() {
    companion object {
        const val EXTRA_TEACHER_LOGIN = "EXTRA_TEACHER_LOGIN"

        fun redirectToSibling(current: BaseActivity, teacherLogin: String) {
            RedirectBuilder
                    .redirect(current)
                    .to(MonitoringTeacherPaymentsActivity::class.java)
                    .withExtra(EXTRA_TEACHER_LOGIN, teacherLogin)
                    .goAndCloseCurrent()
        }
    }

    @Inject lateinit var studentsPaymentsProviderInteractor: StudentsPaymentsProviderInteractor
    @Inject lateinit var alertsMonitoringTeachersService: AlertsMonitoringTeachersInteractor
    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Inject lateinit var staffMembersStorageInteractor: StaffMembersStorageInteractor
    @Inject lateinit var studentsPaymentsAsyncService: StudentsPaymentsActionsInteractor

    lateinit var teacherLogin: String

    private var filterEnabled: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_monitoring_teacher_payments)

        teacherLogin = intent.getStringExtra(EXTRA_TEACHER_LOGIN)!!

        initHeader()

        monitoringTeacherPaymentsSecondaryHeaderView.bind(
                currentItem = MonitoringTeacherHeaderView.Item.PAYMENTS,
                teacherLogin = teacherLogin,
                hasLessonsAlert = alertsMonitoringTeachersService.haveLessonsAlerts(teacherLogin = teacherLogin),
                hasPaymentsAlert = alertsMonitoringTeachersService.havePaymentsAlerts(teacherLogin = teacherLogin),
                hasDebtsAlert = alertsMonitoringTeachersService.haveDebtsAlerts(teacherLogin = teacherLogin)
        )

        monitoringTeacherPaymentsEmptyWithFilterView.bind {
            toggleFilter()
        }

        initList()
    }

    private fun initHeader() {
        monitoringTeacherPaymentsHeaderView
                .setLeftButtonAction { doFinish() }

        monitoringTeacherPaymentsHeaderView.getFirstButtonHandler()
                .setAction(action = { toggleFilter() })
                .setToggled(toggled = filterEnabled)

        monitoringTeacherPaymentsHeaderView.getSecondButtonHandler()
                .setAction(action = {
                    TeacherActivity.redirectToChild(
                            current = this,
                            teacherLogin = teacherLogin
                    )
                })
    }

    private fun toggleFilter() {
        filterEnabled = !filterEnabled

        monitoringTeacherPaymentsHeaderView.getFirstButtonHandler().setToggled(toggled = filterEnabled)

        initList()
    }

    private fun initList() {
        val allPayments = studentsPaymentsProviderInteractor.getForTeacher(
                teacherLogin = teacherLogin,
                onlyUnprocessed = false
        )

        val filteredPayments = allPayments.filter { payment -> !filterEnabled || !payment.processed }

        monitoringTeacherPaymentsEmptyView.visibility = visibleElseGone(visible = allPayments.isEmpty())
        monitoringTeacherPaymentsEmptyWithFilterView.visibility = visibleElseGone(visible = (allPayments.isNotEmpty() && filteredPayments.isEmpty()))

        monitoringTeacherPaymentsView.bind(
                data = PaymentsViewData(
                        paymentItemsData = filteredPayments
                                .sortedByDescending { payment ->
                                    payment.info.time
                                }.map { studentPayment ->
                                    PaymentsItemViewData(
                                            studentPayment = studentPayment,
                                            studentName = studentsStorageInteractor.getByLogin(
                                                    studentPayment.info.studentLogin
                                            )?.person?.name ?: "?",
                                            staffMemberName = staffMembersStorageInteractor.getStaffMember(
                                                    studentPayment.info.staffMemberLogin
                                            )?.person?.name ?: "?",
                                            singleTeacher = false,
                                            clickAction = {
                                                studentsPaymentsAsyncService
                                                        .setPaymentProcessed(studentPayment.id)
                                                        // .onSuccess { renderProcessed(it.processed) } todo
                                            }
                                    )
                                }
                )
        )
    }

    override fun onBackPressed() {
        doFinish()
    }

    private fun doFinish() {
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
