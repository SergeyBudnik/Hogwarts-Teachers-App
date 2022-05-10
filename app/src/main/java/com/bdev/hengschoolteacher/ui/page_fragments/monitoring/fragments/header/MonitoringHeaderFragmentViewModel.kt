package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.header

import androidx.lifecycle.LiveData
import com.bdev.hengschoolteacher.data.common.MutableLiveDataWithState
import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringInteractor
import com.bdev.hengschoolteacher.ui.fragments.BaseFragmentViewModel
import com.bdev.hengschoolteacher.ui.fragments.BaseFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.data.MonitoringPageFragmentTab
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringHeaderFragmentViewModel : BaseFragmentViewModel {
    fun getDataLiveData(): LiveData<MonitoringHeaderFragmentData>

    fun setCurrentItem(tab: MonitoringPageFragmentTab)
}

@HiltViewModel
class MonitoringHeaderFragmentViewModelImpl @Inject constructor(
    private val alertsMonitoringService: AlertsMonitoringInteractor
): MonitoringHeaderFragmentViewModel, BaseFragmentViewModelImpl() {
    private val initialData = getInitialData()

    private val dataLiveData = MutableLiveDataWithState(initialValue = initialData)

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun setCurrentItem(tab: MonitoringPageFragmentTab) {
        dataLiveData.updateValue(defaultValue = initialData) { oldData ->
            oldData.copy(tab = tab)
        }
    }

    private fun getInitialData(): MonitoringHeaderFragmentData =
        MonitoringHeaderFragmentData(
            tab = MonitoringPageFragmentTab.NONE,
            hasLessonsAlert = alertsMonitoringService.lessonsHaveAlerts(),
            hasTeachersAlert = alertsMonitoringService.teachersHaveAlerts(),
            hasStudentsAlert = alertsMonitoringService.studentsHaveAlerts()
        )
}