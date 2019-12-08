package com.bdev.hengschoolteacher.ui.activities.monitoring.teacher

import android.annotation.SuppressLint
import android.view.View
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.teacher.TeacherPaymentsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.monitoring.teacher.MonitoringTeacherHeaderView
import kotlinx.android.synthetic.main.activity_monitoring_teacher_payments.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra

@SuppressLint("Registered")
@EActivity(R.layout.activity_monitoring_teacher_payments)
open class MonitoringTeacherPaymentsActivity : BaseActivity() {
    companion object {
        const val EXTRA_TEACHER_LOGIN = "EXTRA_TEACHER_LOGIN"

        fun redirectToSibling(current: BaseActivity, teacherLogin: String) {
            RedirectBuilder
                    .redirect(current)
                    .to(MonitoringTeacherPaymentsActivity_::class.java)
                    .withExtra(EXTRA_TEACHER_LOGIN, teacherLogin)
                    .goAndCloseCurrent()
        }
    }

    @Bean
    lateinit var teacherPaymentsService: TeacherPaymentsService

    @Extra(EXTRA_TEACHER_LOGIN)
    lateinit var teacherLogin: String

    private var filterEnabled: Boolean = true

    @AfterViews
    fun init() {
        monitoringTeacherPaymentsHeaderView
                .setLeftButtonAction { doFinish() }
                .setFirstRightButtonAction { toggleFilter() }
                .setFirstRightButtonActive(filterEnabled)

        monitoringTeacherPaymentsSecondaryHeaderView.bind(
                currentItem = MonitoringTeacherHeaderView.Item.PAYMENTS,
                teacherLogin = teacherLogin
        )

        monitoringTeacherPaymentsTeacherInfoView.bind(
                teacherLogin = teacherLogin
        )

        monitoringTeacherPaymentsEmptyWithFilterView.bind {
            toggleFilter()
        }

        initList()
    }

    private fun toggleFilter() {
        filterEnabled = !filterEnabled

        monitoringTeacherPaymentsHeaderView.setFirstRightButtonActive(filterEnabled)

        initList()
    }

    private fun initList() {
        val allPayments = teacherPaymentsService.getPayments(
                teacherLogin = teacherLogin,
                onlyUnprocessed = false
        )

        val filteredPayments = allPayments.filter { payment -> !filterEnabled || !payment.processed }

        monitoringTeacherPaymentsEmptyView.visibility =
                if (allPayments.isEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

        monitoringTeacherPaymentsEmptyWithFilterView.visibility =
                if (!allPayments.isEmpty() && filteredPayments.isEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

        monitoringTeacherPaymentsView.bind(
                payments = filteredPayments,
                singleTeacher = true,
                editable = true
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
