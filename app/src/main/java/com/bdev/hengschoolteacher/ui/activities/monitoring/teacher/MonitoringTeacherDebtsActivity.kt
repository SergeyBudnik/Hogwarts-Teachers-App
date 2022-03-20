package com.bdev.hengschoolteacher.ui.activities.monitoring.teacher

import android.os.Bundle
import android.os.PersistableBundle
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringTeachersInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_debts.StudentsDebtsInteractor
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.teacher.TeacherActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.monitoring.teacher.MonitoringTeacherHeaderView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_monitoring_teacher_debts.*
import javax.inject.Inject

@AndroidEntryPoint
class MonitoringTeacherDebtsActivity : BaseActivity() {
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
                    .to(MonitoringTeacherDebtsActivity::class.java)
                    .withExtra(EXTRA_TEACHER_LOGIN, teacherLogin)
        }
    }

    lateinit var teacherLogin: String

    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Inject lateinit var studentsDebtsInteractor: StudentsDebtsInteractor
    @Inject lateinit var alertsMonitoringTeachersService: AlertsMonitoringTeachersInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_monitoring_teacher_debts)

        teacherLogin = intent.getStringExtra(EXTRA_TEACHER_LOGIN)!!

        initHeader()

        monitoringTeacherDebtsSecondaryHeaderView.bind(
                currentItem = MonitoringTeacherHeaderView.Item.DEBTS,
                teacherLogin = teacherLogin,
                hasLessonsAlert = alertsMonitoringTeachersService.haveLessonsAlerts(teacherLogin = teacherLogin),
                hasPaymentsAlert = alertsMonitoringTeachersService.havePaymentsAlerts(teacherLogin = teacherLogin),
                hasDebtsAlert = alertsMonitoringTeachersService.haveDebtsAlerts(teacherLogin = teacherLogin)
        )

        monitoringTeacherDebtsListView.bind(
                studentsToExpectedDebt = studentsStorageInteractor.getAll()
                        .filter { it.managerLogin == teacherLogin }
                        .map {
                            Pair(it, studentsDebtsInteractor.getExpectedDebt(studentLogin = it.login))
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