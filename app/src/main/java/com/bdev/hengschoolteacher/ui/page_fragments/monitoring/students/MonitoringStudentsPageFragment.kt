package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.students

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderInteractor
import com.bdev.hengschoolteacher.interactors.students_debts.StudentsDebtsInteractor
import com.bdev.hengschoolteacher.ui.fragments.app_menu.AppMenuFragment
import com.bdev.hengschoolteacher.ui.fragments.app_menu.data.AppMenuItem
import com.bdev.hengschoolteacher.ui.fragments.monitoring.header.MonitoringHeaderFragment
import com.bdev.hengschoolteacher.ui.fragments.monitoring.header.data.MonitoringHeaderFragmentItem
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_monitoring_students.*
import javax.inject.Inject

@AndroidEntryPoint
class MonitoringStudentsPageFragment : BasePageFragment<MonitoringStudentsPageFragmentViewModel>() {
    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Inject lateinit var studentsAttendancesProviderInteractor: StudentsAttendancesProviderInteractor
    @Inject lateinit var studentsDebtsInteractor: StudentsDebtsInteractor
    @Inject lateinit var alertsMonitoringService: AlertsMonitoringInteractor

    private var filterEnabled = true

    private var search = ""

    override fun provideViewModel(): MonitoringStudentsPageFragmentViewModel =
        ViewModelProvider(this).get(MonitoringStudentsPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_monitoring_students, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        initHeader()

        getMenuFragment().setCurrentItem(item = AppMenuItem.MONITORING)

        getSecondaryHeaderFragment().setCurrentItem(
            item = MonitoringHeaderFragmentItem.STUDENTS
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

    private fun getSecondaryHeaderFragment(): MonitoringHeaderFragment =
        childFragmentManager.findFragmentById(R.id.monitoringPaymentsSecondaryHeaderFragment) as MonitoringHeaderFragment

    private fun getMenuFragment(): AppMenuFragment =
        childFragmentManager.findFragmentById(R.id.appMenuFragment) as AppMenuFragment
}