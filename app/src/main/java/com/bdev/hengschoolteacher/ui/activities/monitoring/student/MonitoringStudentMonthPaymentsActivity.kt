package com.bdev.hengschoolteacher.ui.activities.monitoring.student

import android.os.Bundle
import android.os.PersistableBundle
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.Month
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_payments.StudentsPaymentsProviderInteractor
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.monitoring.student.MonitoringStudentMonthHeaderView
import com.bdev.hengschoolteacher.ui.views.app.payments.PaymentsItemViewData
import com.bdev.hengschoolteacher.ui.views.app.payments.PaymentsViewData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_monitoring_student_month_payments.*
import javax.inject.Inject

@AndroidEntryPoint
class MonitoringStudentMonthPaymentsActivity : BaseActivity() {
    companion object {
        const val EXTRA_STUDENT_LOGIN = "EXTRA_STUDENT_LOGIN"
        const val EXTRA_MONTH_INDEX = "EXTRA_MONTH_INDEX"

        fun redirectToSibling(current: BaseActivity, studentLogin: String, monthIndex: Int) {
            redirect(current = current, studentLogin = studentLogin, monthIndex = monthIndex)
                    .goAndCloseCurrent()
        }

        private fun redirect(current: BaseActivity, studentLogin: String, monthIndex: Int): RedirectBuilder {
            return RedirectBuilder
                    .redirect(current)
                    .to(MonitoringStudentMonthPaymentsActivity::class.java)
                    .withExtra(EXTRA_STUDENT_LOGIN, studentLogin)
                    .withExtra(EXTRA_MONTH_INDEX, monthIndex)
        }
    }

    lateinit var studentLogin: String
    var monthIndex: Int = 0

    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Inject lateinit var studentsPaymentsProviderInteractor: StudentsPaymentsProviderInteractor
    @Inject lateinit var staffMembersStorageInteractor: StaffMembersStorageInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_monitoring_student_month_payments)

        studentLogin = intent.getStringExtra(EXTRA_STUDENT_LOGIN)!!
        monthIndex = intent.getIntExtra(EXTRA_MONTH_INDEX, 0)

        val month = Month.findByIndex(monthIndex)

        monitoringStudentMonthPaymentsHeaderView
                .setTitle("Мониторинг. Студент. ${getString(month.nameId)}")
                .setLeftButtonAction { doFinish() }

        monitoringStudentMonthPaymentsSecondaryHeaderView.bind(
                studentLogin = studentLogin,
                monthIndex = monthIndex,
                item = MonitoringStudentMonthHeaderView.Item.PAYMENTS
        )

        val student = studentsStorageInteractor.getByLogin(studentLogin)

        student?.let { monitoringStudentMonthPaymentsStudentView.bind(it) }

        monitoringStudentMonthPaymentsPaymentsView.bind(
                data = PaymentsViewData(
                        paymentItemsData = studentsPaymentsProviderInteractor
                                .getForStudentForMonth(
                                        studentLogin = studentLogin,
                                        monthIndex = monthIndex
                                ).sortedByDescending { payment ->
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
                                            clickAction = null
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
