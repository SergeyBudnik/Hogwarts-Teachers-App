package com.bdev.hengschoolteacher.ui.activities.monitoring

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringInteractorImpl
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractorImpl
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderServiceImpl
import com.bdev.hengschoolteacher.interactors.students_debts.StudentDebtsService
import com.bdev.hengschoolteacher.interactors.students_debts.StudentDebtsServiceImpl
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import com.bdev.hengschoolteacher.ui.views.app.monitoring.MonitoringHeaderView
import kotlinx.android.synthetic.main.activity_monitoring_students.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@SuppressLint("Registered")
@EActivity(R.layout.activity_monitoring_students)
open class MonitoringStudentsActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(MonitoringStudentsActivity_::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Bean(StudentsStorageInteractorImpl::class)
    lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Bean
    lateinit var studentsAttendancesProviderService: StudentsAttendancesProviderServiceImpl
    @Bean(StudentDebtsServiceImpl::class)
    lateinit var studentsDebtsService: StudentDebtsService
    @Bean
    lateinit var alertsMonitoringService: AlertsMonitoringInteractorImpl

    private var filterEnabled = true

    private var search = ""

    @AfterViews
    fun init() {
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
                    Pair(it, studentsDebtsService.getExpectedDebt(studentLogin = it.login))
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
