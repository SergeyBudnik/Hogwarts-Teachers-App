package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.teachers

import com.bdev.hengschoolteacher.data.common.NullableMutableLiveDataWithState
import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringTeachersInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.data.MonitoringPageFragmentTab
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.MonitoringContentFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.MonitoringContentFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.teachers.data.MonitoringTeachersItemData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringTeachersPageFragmentViewModel : MonitoringContentFragmentViewModel<MonitoringTeachersFragmentData>

@HiltViewModel
class MonitoringTeachersPageFragmentViewModelImpl @Inject constructor(
    private val staffMembersStorageInteractor: StaffMembersStorageInteractor,
    private val alertsMonitoringTeachersInteractor: AlertsMonitoringTeachersInteractor
): MonitoringTeachersPageFragmentViewModel, MonitoringContentFragmentViewModelImpl<MonitoringTeachersFragmentData>() {
    private val initialData = getInitialData()

    private val dataLiveData = NullableMutableLiveDataWithState(
        initialValue = initialData
    )

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun isVisible() = dataLiveData.getValue()?.visible ?: false

    override fun setCurrentTab(tab: MonitoringPageFragmentTab) {
        dataLiveData.updateValue(defaultValue = initialData) { oldValue ->
            oldValue.copy(visible = (tab == MonitoringPageFragmentTab.TEACHERS))
        }
    }

    private fun getInitialData(): MonitoringTeachersFragmentData =
        MonitoringTeachersFragmentData(
            visible = false,
            teachers = staffMembersStorageInteractor.getAllStaffMembers().map {
                MonitoringTeachersItemData(
                    staffMember = it,
                    hasAlerts = alertsMonitoringTeachersInteractor.haveAlerts(
                        teacherLogin = it.login
                    )
                )
            }
        )
}