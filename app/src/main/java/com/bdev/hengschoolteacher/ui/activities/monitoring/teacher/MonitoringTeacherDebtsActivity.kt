package com.bdev.hengschoolteacher.ui.activities.monitoring.teacher

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.services.alerts.monitoring.AlertsMonitoringTeachersService
import com.bdev.hengschoolteacher.services.students.StudentsStorageService
import com.bdev.hengschoolteacher.services.students.StudentsStorageServiceImpl
import com.bdev.hengschoolteacher.services.students_debts.StudentDebtsService
import com.bdev.hengschoolteacher.services.students_debts.StudentDebtsServiceImpl
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.teacher.TeacherActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.monitoring.teacher.MonitoringTeacherHeaderView
import kotlinx.android.synthetic.main.activity_monitoring_teacher_debts.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra

@SuppressLint("Registered")
@EActivity(R.layout.activity_monitoring_teacher_debts)
open class MonitoringTeacherDebtsActivity : BaseActivity() {
    companion object {
        const val EXTRA_TEACHER_LOGIN = "EXTRA_TEACHER_LOGIN"

        fun redirectToChild(current: BaseActivity, teacherLogin: String) {
            redirect(current = current, teacherLogin = teacherLogin)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .go()
        }

        fun redirectToSibling(current: BaseActivity, teacherLogin: String) {
            redirect(current = current, teacherLogin = teacherLogin)
                    .goAndCloseCurrent()
        }

        private fun redirect(current: BaseActivity, teacherLogin: String): RedirectBuilder {
            return RedirectBuilder
                    .redirect(current)
                    .to(MonitoringTeacherDebtsActivity_::class.java)
                    .withExtra(EXTRA_TEACHER_LOGIN, teacherLogin)
        }
    }

    @Extra(EXTRA_TEACHER_LOGIN)
    lateinit var teacherLogin: String

    @Bean(StudentsStorageServiceImpl::class)
    lateinit var studentsStorageService: StudentsStorageService
    @Bean(StudentDebtsServiceImpl::class)
    lateinit var studentsDebtsService: StudentDebtsService
    @Bean
    lateinit var alertsMonitoringTeachersService: AlertsMonitoringTeachersService

    @AfterViews
    fun init() {
        initHeader()

        monitoringTeacherDebtsSecondaryHeaderView.bind(
                currentItem = MonitoringTeacherHeaderView.Item.DEBTS,
                teacherLogin = teacherLogin,
                hasLessonsAlert = alertsMonitoringTeachersService.haveLessonsAlerts(teacherLogin = teacherLogin),
                hasPaymentsAlert = alertsMonitoringTeachersService.havePaymentsAlerts(teacherLogin = teacherLogin),
                hasDebtsAlert = alertsMonitoringTeachersService.haveDebtsAlerts(teacherLogin = teacherLogin)
        )

        monitoringTeacherDebtsListView.bind(
                studentsToExpectedDebt = studentsStorageService.getAll()
                        .filter { it.managerLogin == teacherLogin }
                        .map {
                            Pair(it, studentsDebtsService.getExpectedDebt(studentLogin = it.login))
                        },
                searchQuery = "",
                withDebtsOnly = true
        )
    }

    override fun getAppLayoutView(): AppLayoutView = monitoringTeacherDebtsRootView

    private fun initHeader() {
        monitoringTeacherDebtsHeaderView
                .setLeftButtonAction { doFinish() }

        monitoringTeacherDebtsHeaderView.getFirstButtonHandler()
                .setAction(action = {
                    TeacherActivity.redirectToChild(
                            current = this,
                            teacherLogin = teacherLogin
                    )
                })
    }

    private fun doFinish() {
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }
}