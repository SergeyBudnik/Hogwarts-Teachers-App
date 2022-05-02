package com.bdev.hengschoolteacher.ui.fragments.monitoring.header

import androidx.lifecycle.LiveData
import com.bdev.hengschoolteacher.data.common.MutableLiveDataWithState
import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringInteractor
import com.bdev.hengschoolteacher.ui.fragments.BaseFragmentViewModel
import com.bdev.hengschoolteacher.ui.fragments.BaseFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.fragments.monitoring.header.data.MonitoringHeaderFragmentItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringHeaderFragmentViewModel : BaseFragmentViewModel {
    fun getDataLiveData(): LiveData<MonitoringHeaderFragmentData>

    fun setCurrentItem(item: MonitoringHeaderFragmentItem)
}

@HiltViewModel
class MonitoringHeaderFragmentViewModelImpl @Inject constructor(
    private val alertsMonitoringService: AlertsMonitoringInteractor
): MonitoringHeaderFragmentViewModel, BaseFragmentViewModelImpl() {
    private val dataLiveData = MutableLiveDataWithState(
        initialValue = getInitialData()
    )

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun setCurrentItem(item: MonitoringHeaderFragmentItem) {
        dataLiveData.updateValue { oldValue ->
            oldValue.copy(currentItem = item)
        }
    }

    private fun getInitialData(): MonitoringHeaderFragmentData =
        MonitoringHeaderFragmentData(
            currentItem = MonitoringHeaderFragmentItem.LESSONS,
            hasLessonsAlert = alertsMonitoringService.lessonsHaveAlerts(),
            hasTeachersAlert = alertsMonitoringService.teachersHaveAlerts(),
            hasStudentsAlert = alertsMonitoringService.studentsHaveAlerts()
        )
}