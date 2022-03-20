package com.bdev.hengschoolteacher.ui.activities.monitoring

import android.os.Bundle
import android.os.PersistableBundle
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderInteractor
import com.bdev.hengschoolteacher.interactors.students_debts.StudentsDebtsInteractor
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import com.bdev.hengschoolteacher.ui.views.app.monitoring.MonitoringHeaderView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_monitoring_students.*
import javax.inject.Inject

@AndroidEntryPoint
class MonitoringStudentsActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(MonitoringStudentsActivity::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Inject lateinit var studentsAttendancesProviderInteractor: StudentsAttendancesProviderInteractor
    @Inject lateinit var studentsDebtsInteractor: StudentsDebtsInteractor
    @Inject lateinit var alertsMonitoringService: AlertsMonitoringInteractor

    private var filterEnabled = true

    private var search = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_monitoring_students)

        initHeader()

        monitoringPaymentsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MONITORING)

        monitoringPaymentsSecondaryHeaderView.bind(
                currentItem = MonitoringHeaderView.Item.STUDENTS,
                hasTeachersAlert = alertsMonitoringService.teachersHaveAlerts(),
                hasStudentsAlert = alertsMonitoringService.studentsHaveAlerts(),
                hasLessonsAlert = alertsMonitoringService.lessonsHaveAlerts()
        )

        monitoringPaymentsHeaderSearchView.addOnTextChangeListener { search ->
            this.search = search.trim()

            initList()
        }

        initList()
    }

    private fun initHeader() {
        monitoringPaymentsHeaderView
                .setLeftButtonAction { monitoringPaymentsMenuLayoutView.openMenu() }

        monitoringPaymentsHeaderView.getFirstButtonHandler()
                .setAction(action = { monitoringPaymentsHeaderSearchView.show() })

        monitoringPaymentsHeaderView.getSecondButtonHandler()
                .setAction(action = { toggleFilter() })
                .setToggled(toggled = filterEnabled)
    }

    private fun initList() {
        monitoringPaymentsListView.bind(
                studentsToExpectedDebt = studentsStorageInteractor.getAll().map {
                    Pair(it, studentsDebtsInteractor.getExpectedDebt(studentLogin = it.login))
                },
                searchQuery = search,
                withDebtsOnly = filterEnabled
        )
    }

    private fun toggleFilter() {
        filterEnabled = !filterEnabled

        monitoringPaymentsHeaderView.getSecondButtonHandler().setToggled(toggled = filterEnabled)

        initList()
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
