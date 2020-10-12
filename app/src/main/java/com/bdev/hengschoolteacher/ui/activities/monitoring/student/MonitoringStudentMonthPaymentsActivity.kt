package com.bdev.hengschoolteacher.ui.activities.monitoring.student

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.Month
import com.bdev.hengschoolteacher.service.StudentsPaymentsService
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.monitoring.student.MonitoringStudentMonthHeaderView
import kotlinx.android.synthetic.main.activity_monitoring_student_month_payments.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra

@SuppressLint("Registered")
@EActivity(R.layout.activity_monitoring_student_month_payments)
open class MonitoringStudentMonthPaymentsActivity : BaseActivity() {
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
                    .to(MonitoringStudentMonthPaymentsActivity_::class.java)
                    .withExtra(EXTRA_STUDENT_LOGIN, studentLogin)
                    .withExtra(EXTRA_MONTH_INDEX, monthIndex)
        }
    }

    @Extra(EXTRA_STUDENT_LOGIN)
    lateinit var studentLogin: String

    @Extra(EXTRA_MONTH_INDEX)
    @JvmField
    var monthIndex: Int = 0

    @Bean
    lateinit var studentsService: StudentsService
    @Bean
    lateinit var studentPaymentsService: StudentsPaymentsService

    @AfterViews
    fun init() {
        val month = Month.findByIndex(monthIndex)

        monitoringStudentMonthPaymentsHeaderView
                .setTitle("Мониторинг. Студент. ${getString(month.nameId)}")
                .setLeftButtonAction { doFinish() }

        monitoringStudentMonthPaymentsSecondaryHeaderView.bind(
                studentLogin = studentLogin,
                monthIndex = monthIndex,
                item = MonitoringStudentMonthHeaderView.Item.PAYMENTS
        )

        val student = studentsService.getStudent(studentLogin)

        student?.let { monitoringStudentMonthPaymentsStudentView.bind(it) }

        monitoringStudentMonthPaymentsPaymentsView.bind(
                payments = studentPaymentsService.getMonthPayments(studentLogin, monthIndex),
                singleTeacher = false,
                editable = false
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
