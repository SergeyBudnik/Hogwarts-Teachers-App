package com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.fragments.header

import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringTeachersInteractor
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.CommonContentHeaderFragmentData
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.CommonContentHeaderFragmentViewModel
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.CommonContentHeaderFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.data.MonitoringTeacherPageFragmentTab
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringTeacherHeaderFragmentViewModel : CommonContentHeaderFragmentViewModel<MonitoringTeacherPageFragmentTab> {
    fun bind(teacherLogin: String)
}

@HiltViewModel
class MonitoringTeacherHeaderFragmentViewModelImpl @Inject constructor(
    private val alertsMonitoringTeachersService: AlertsMonitoringTeachersInteractor
): MonitoringTeacherHeaderFragmentViewModel, CommonContentHeaderFragmentViewModelImpl<MonitoringTeacherPageFragmentTab>(
    initialData =  CommonContentHeaderFragmentData(
        tab = MonitoringTeacherPageFragmentTab.LESSONS,
        tabAlerts = emptyMap()
    )
) {
    override fun bind(teacherLogin: String) {
        dataLiveData.setValue(mutator = { oldValue ->
            oldValue.copy(
                tabAlerts = mapOf(
                    Pair(
                        MonitoringTeacherPageFragmentTab.LESSONS,
                        alertsMonitoringTeachersService.haveLessonsAlerts(teacherLogin = teacherLogin)
                    ),
                    Pair(
                        MonitoringTeacherPageFragmentTab.PAYMENTS,
                        alertsMonitoringTeachersService.havePaymentsAlerts(teacherLogin = teacherLogin)
                    ),
                    Pair(
                        MonitoringTeacherPageFragmentTab.DEBTS,
                        alertsMonitoringTeachersService.haveDebtsAlerts(teacherLogin = teacherLogin)
                    )
                )
            )
        })
    }
}