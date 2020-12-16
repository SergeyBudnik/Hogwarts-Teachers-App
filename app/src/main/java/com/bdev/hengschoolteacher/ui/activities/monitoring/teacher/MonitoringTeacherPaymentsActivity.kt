package com.bdev.hengschoolteacher.ui.activities.monitoring.teacher

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.services.students_payments.StudentsPaymentsProviderService
import com.bdev.hengschoolteacher.services.students_payments.StudentsPaymentsProviderServiceImpl
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.teacher.TeacherActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
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

    @Bean(StudentsPaymentsProviderServiceImpl::class)
    lateinit var studentsPaymentsProviderService: StudentsPaymentsProviderService

    @Extra(EXTRA_TEACHER_LOGIN)
    lateinit var teacherLogin: String

    private var filterEnabled: Boolean = true

    @AfterViews
    fun init() {
        initHeader()

        monitoringTeacherPaymentsSecondaryHeaderView.bind(
                currentItem = MonitoringTeacherHeaderView.Item.PAYMENTS,
                teacherLogin = teacherLogin
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
        val allPayments = studentsPaymentsProviderService.getForTeacher(
                teacherLogin = teacherLogin,
                onlyUnprocessed = false
        )

        val filteredPayments = allPayments.filter { payment -> !filterEnabled || !payment.processed }

        monitoringTeacherPaymentsEmptyView.visibility = visibleElseGone(visible = allPayments.isEmpty())
        monitoringTeacherPaymentsEmptyWithFilterView.visibility = visibleElseGone(visible = (allPayments.isNotEmpty() && filteredPayments.isEmpty()))

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
