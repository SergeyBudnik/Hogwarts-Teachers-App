package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.teachers

import androidx.lifecycle.LiveData
import com.bdev.hengschoolteacher.data.common.MutableLiveDataWithState
import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringTeachersInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.teachers.data.MonitoringTeachersItemData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringTeachersPageFragmentViewModel : BasePageFragmentViewModel {
    fun getDataLiveData(): LiveData<MonitoringTeachersPageFragmentData>
}

@HiltViewModel
class MonitoringTeachersPageFragmentViewModelImpl @Inject constructor(
    private val staffMembersStorageInteractor: StaffMembersStorageInteractor,
    private val alertsMonitoringTeachersInteractor: AlertsMonitoringTeachersInteractor
): MonitoringTeachersPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    private val dataLiveData = MutableLiveDataWithState(
        initialValue = getInitialData()
    )

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun goBack() {
        // todo
    }

    private fun getInitialData(): MonitoringTeachersPageFragmentData =
        MonitoringTeachersPageFragmentData(
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