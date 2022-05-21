package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.header

import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringInteractor
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.CommonContentHeaderFragmentData
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.CommonContentHeaderFragmentViewModel
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.CommonContentHeaderFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.data.MonitoringPageFragmentTab
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringHeaderFragmentViewModel : CommonContentHeaderFragmentViewModel<MonitoringPageFragmentTab>

@HiltViewModel
class MonitoringHeaderFragmentViewModelImpl @Inject constructor(
    alertsMonitoringService: AlertsMonitoringInteractor
): MonitoringHeaderFragmentViewModel, CommonContentHeaderFragmentViewModelImpl<MonitoringPageFragmentTab>(
    initialData = CommonContentHeaderFragmentData(
        tab = MonitoringPageFragmentTab.NONE,
        tabAlerts = mapOf(
            Pair(MonitoringPageFragmentTab.LESSONS, alertsMonitoringService.lessonsHaveAlerts()),
            Pair(MonitoringPageFragmentTab.TEACHERS, alertsMonitoringService.teachersHaveAlerts()),
            Pair(MonitoringPageFragmentTab.STUDENTS, alertsMonitoringService.studentsHaveAlerts())
        )
    )
)