package com.bdev.hengschoolteacher.ui.activities.monitoring.teacher

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
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

    @Bean
    lateinit var studentsService: StudentsService

    @AfterViews
    fun init() {
        monitoringTeacherDebtsHeaderView
                .setLeftButtonAction { doFinish() }

        monitoringTeacherDebtsSecondaryHeaderView.bind(
                currentItem = MonitoringTeacherHeaderView.Item.DEBTS,
                teacherLogin = teacherLogin
        )

        monitoringTeacherDebtsTeacherInfoView.bind(
                teacherLogin = teacherLogin
        )

        monitoringTeacherDebtsListView.bind(
                students = studentsService.getAllStudents().filter { it.managerLogin == teacherLogin },
                searchQuery = "",
                withDebtsOnly = true
        )
    }

    override fun getAppLayoutView(): AppLayoutView = monitoringTeacherDebtsRootView

    private fun doFinish() {
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }
}