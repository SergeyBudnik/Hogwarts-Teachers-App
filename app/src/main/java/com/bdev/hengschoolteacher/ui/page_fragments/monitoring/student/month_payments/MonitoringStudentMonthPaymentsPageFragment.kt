package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.student.month_payments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.Month
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_payments.StudentsPaymentsProviderInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.views.app.root.HtPageRootView
import com.bdev.hengschoolteacher.ui.views.app.monitoring.student.MonitoringStudentMonthHeaderView
import com.bdev.hengschoolteacher.ui.views.app.payments.PaymentsItemViewData
import com.bdev.hengschoolteacher.ui.views.app.payments.PaymentsViewData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_monitoring_student_month_payments.*
import javax.inject.Inject

@AndroidEntryPoint
class MonitoringStudentMonthPaymentsPageFragment : BasePageFragment<MonitoringStudentMonthPaymentsPageFragmentViewModel>() {
    companion object {
        const val EXTRA_STUDENT_LOGIN = "EXTRA_STUDENT_LOGIN"
        const val EXTRA_MONTH_INDEX = "EXTRA_MONTH_INDEX"
    }

    lateinit var studentLogin: String
    var monthIndex: Int = 0

    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Inject lateinit var studentsPaymentsProviderInteractor: StudentsPaymentsProviderInteractor
    @Inject lateinit var staffMembersStorageInteractor: StaffMembersStorageInteractor

    override fun provideViewModel(): MonitoringStudentMonthPaymentsPageFragmentViewModel =
        ViewModelProvider(this).get(MonitoringStudentMonthPaymentsPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_monitoring_student_month_payments, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        // studentLogin = intent.getStringExtra(EXTRA_STUDENT_LOGIN)!! todo
        // monthIndex = intent.getIntExtra(EXTRA_MONTH_INDEX, 0) todo

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

    private fun doFinish() {
//        finish()
//        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }
}
