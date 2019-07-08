package com.bdev.hengschoolteacher.ui.activities.monitoring.teacher

import android.annotation.SuppressLint
import android.view.View
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.HeaderElementsUtils
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import kotlinx.android.synthetic.main.activity_monitoring_teacher_salary.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra

@SuppressLint("Registered")
@EActivity(R.layout.activity_monitoring_teacher_salary)
open class MonitoringTeacherSalaryActivity : BaseActivity() {
    companion object {
        const val EXTRA_TEACHER_ID = "EXTRA_TEACHER_ID"

        fun redirectToSibling(current: BaseActivity, teacherId: Long) {
            return RedirectBuilder
                    .redirect(current)
                    .to(MonitoringTeacherSalaryActivity_::class.java)
                    .withExtra(EXTRA_TEACHER_ID, teacherId)
                    .goAndCloseCurrent()
        }
    }

    @Extra(EXTRA_TEACHER_ID)
    @JvmField
    var teacherId: Long = 0

    private var calendarEnabled = false

    @AfterViews
    fun init() {
        monitoringTeacherSalaryHeaderView
                .setLeftButtonAction { doFinish() }
                .setFirstRightButtonAction { toggleCalendar() }
                .setFirstRightButtonColor(getHeaderButtonColor(calendarEnabled))

        monitoringTeacherSalarySecondaryHeaderView.bind(
                teacherId = teacherId
        )

        monitoringTeacherSalaryTeacherInfoView.bind(
                teacherId = teacherId
        )

        monitoringTeacherSalaryWeekSelectionBarView.init { weekIndex ->
            monitoringTeacherSalaryTeacherSalaryView.init(
                    teacherId = teacherId,
                    weekIndex = weekIndex
            )
        }
    }

    override fun onBackPressed() {
        doFinish()
    }

    private fun doFinish() {
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }

    private fun toggleCalendar() {
        calendarEnabled = !calendarEnabled

        monitoringTeacherSalaryHeaderView.setFirstRightButtonColor(getHeaderButtonColor(calendarEnabled))

        monitoringTeacherSalaryWeekSelectionBarView.visibility = if (calendarEnabled) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun getHeaderButtonColor(enabled: Boolean): Int {
        return HeaderElementsUtils.getColor(this, enabled)
    }
}
