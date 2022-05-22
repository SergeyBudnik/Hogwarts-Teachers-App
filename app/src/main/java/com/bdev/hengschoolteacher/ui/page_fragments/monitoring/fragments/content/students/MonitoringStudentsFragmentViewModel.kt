package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.students

import com.bdev.hengschoolteacher.data.common.NullableMutableLiveDataWithState
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.data.MonitoringPageFragmentTab
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.MonitoringContentFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.MonitoringContentFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringStudentsPageFragmentViewModel : MonitoringContentFragmentViewModel<MonitoringStudentsFragmentData>

@HiltViewModel
class MonitoringStudentsPageFragmentViewModelImpl @Inject constructor(
): MonitoringStudentsPageFragmentViewModel, MonitoringContentFragmentViewModelImpl<MonitoringStudentsFragmentData>() {
    private val initialData = MonitoringStudentsFragmentData(
        visible = false
    )

    private val dataLiveData = NullableMutableLiveDataWithState(
        initialValue = initialData
    )

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun isVisible() = dataLiveData.getValue()?.visible ?: false

    override fun setCurrentTab(tab: MonitoringPageFragmentTab) {
        dataLiveData.updateValue(defaultValue = initialData) { oldValue ->
            oldValue.copy(visible = (tab == MonitoringPageFragmentTab.STUDENTS))
        }
    }
}