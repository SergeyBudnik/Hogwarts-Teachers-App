package com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.debts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringTeachersInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_debts.StudentsDebtsInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.views.app.monitoring.teacher.MonitoringTeacherHeaderView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_monitoring_teacher_debts.*
import javax.inject.Inject

@AndroidEntryPoint
class MonitoringTeacherDebtsPageFragment : BasePageFragment<MonitoringTeacherDebtsPageFragmentViewModel>() {
    companion object {
        const val EXTRA_TEACHER_LOGIN = "EXTRA_TEACHER_LOGIN"
    }

    lateinit var teacherLogin: String

    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Inject lateinit var studentsDebtsInteractor: StudentsDebtsInteractor
    @Inject lateinit var alertsMonitoringTeachersService: AlertsMonitoringTeachersInteractor

    override fun provideViewModel(): MonitoringTeacherDebtsPageFragmentViewModel =
        ViewModelProvider(this).get(MonitoringTeacherDebtsPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_monitoring_teacher_debts, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        // teacherLogin = intent.getStringExtra(EXTRA_TEACHER_LOGIN)!! todo

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

    private fun initHeader() {
        monitoringTeacherDebtsHeaderView
                .setLeftButtonAction { doFinish() }

        monitoringTeacherDebtsHeaderView.getFirstButtonHandler()
                .setAction(action = {
//                    TeacherPageFragment.redirectToChild(
//                            current = this,
//                            teacherLogin = teacherLogin
//                    )
                })
    }

    private fun doFinish() {
//        finish()
//        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }
}