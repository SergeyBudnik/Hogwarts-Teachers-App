package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.students

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
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.MonitoringContentFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_monitoring_students.*
import javax.inject.Inject

@AndroidEntryPoint
class MonitoringStudentsFragment : MonitoringContentFragment<MonitoringStudentsPageFragmentViewModel>() {
    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Inject lateinit var studentsAttendancesProviderInteractor: StudentsAttendancesProviderInteractor
    @Inject lateinit var studentsDebtsInteractor: StudentsDebtsInteractor
    @Inject lateinit var alertsMonitoringService: AlertsMonitoringInteractor

    override fun provideViewModel(): MonitoringStudentsPageFragmentViewModel =
        ViewModelProvider(this).get(MonitoringStudentsPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_monitoring_students, container, false)

    override fun getRootView(): View = monitoringStudentsRootView

    override fun doOnViewCreated() {
        super.doOnViewCreated()

//        monitoringPaymentsHeaderSearchView.addOnTextChangeListener { search ->
//            this.search = search.trim()
//
//            initList()
//        }

        initList()
    }

//    private fun initHeader() {
//        monitoringPaymentsHeaderView
//                .setLeftButtonAction { monitoringPaymentsMenuLayoutView.openMenu() }
//
//        monitoringPaymentsHeaderView.getFirstButtonHandler()
//                .setAction(action = { monitoringPaymentsHeaderSearchView.show() })
//
//        monitoringPaymentsHeaderView.getSecondButtonHandler()
//                .setAction(action = { toggleFilter() })
//                .setToggled(toggled = filterEnabled)
//    }

    private fun initList() {
//        monitoringStudentsListView.bind(
//                studentsToExpectedDebt = studentsStorageInteractor.getAll().map {
//                    Pair(it, studentsDebtsInteractor.getExpectedDebt(studentLogin = it.login))
//                },
//                searchQuery = search,
//                withDebtsOnly = filterEnabled
//        )
    }
}