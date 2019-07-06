package com.bdev.hengschoolteacher.ui.activities.monitoring.student

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.Month
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.monitoring.student.MonitoringStudentMonthHeaderView
import kotlinx.android.synthetic.main.activity_monitoring_student_month_attendance.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra

@SuppressLint("Registered")
@EActivity(R.layout.activity_monitoring_student_month_attendance)
open class MonitoringStudentMonthAttendanceActivity : BaseActivity() {
    companion object {
        const val EXTRA_STUDENT_ID = "EXTRA_STUDENT_ID"
        const val EXTRA_MONTH_INDEX = "EXTRA_MONTH_INDEX"

        fun redirectToChild(current: BaseActivity, studentId: Long, monthIndex: Int) {
            redirect(current = current, studentId = studentId, monthIndex = monthIndex)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .go()
        }

        fun redirectToSibling(current: BaseActivity, studentId: Long, monthIndex: Int) {
            redirect(current = current, studentId = studentId, monthIndex = monthIndex)
                    .goAndCloseCurrent()
        }

        private fun redirect(current: BaseActivity, studentId: Long, monthIndex: Int): RedirectBuilder {
            return RedirectBuilder
                    .redirect(current)
                    .to(MonitoringStudentMonthAttendanceActivity_::class.java)
                    .withExtra(EXTRA_STUDENT_ID, studentId)
                    .withExtra(EXTRA_MONTH_INDEX, monthIndex)
        }
    }

    @Extra(EXTRA_STUDENT_ID)
    @JvmField
    var studentId: Long = 0

    @Extra(EXTRA_MONTH_INDEX)
    @JvmField
    var monthIndex: Int = 0

    @Bean
    lateinit var studentsService: StudentsService

    @AfterViews
    fun init() {
        val month = Month.findByIndex(monthIndex)

        monitoringStudentMonthAttendanceHeaderView
                .setTitle("Мониторинг. Студент. ${getString(month.nameId)}")
                .setLeftButtonAction { doFinish() }

        monitoringStudentMonthAttendanceSecondaryHeaderView
                .bind(studentId = studentId, monthIndex = monthIndex)
                .setItem(MonitoringStudentMonthHeaderView.Item.ATTENDANCE)

        val student = studentsService.getStudent(studentId)

        student?.let { monitoringStudentMonthAttendanceStudentView.bind(it) }
    }

    override fun onBackPressed() {
        doFinish()
    }

    private fun doFinish() {
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }
}
