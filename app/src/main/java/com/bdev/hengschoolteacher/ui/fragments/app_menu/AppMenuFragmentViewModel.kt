package com.bdev.hengschoolteacher.ui.fragments.app_menu

import androidx.lifecycle.LiveData
import com.bdev.hengschoolteacher.data.common.MutableLiveDataWithState
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringInteractor
import com.bdev.hengschoolteacher.interactors.alerts.profile.AlertsProfileInteractor
import com.bdev.hengschoolteacher.interactors.profile.ProfileInteractor
import com.bdev.hengschoolteacher.ui.fragments.BaseFragmentViewModel
import com.bdev.hengschoolteacher.ui.fragments.BaseFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.fragments.app_menu.data.AppMenuItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface AppMenuFragmentViewModel : BaseFragmentViewModel {
    fun getDataLiveData(): LiveData<AppMenuFragmentData>

    fun setItem(item: AppMenuItem)
}

@HiltViewModel
class AppMenuFragmentViewModelImpl @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val alertsProfileInteractor: AlertsProfileInteractor,
    private val alertsMonitoringInteractor: AlertsMonitoringInteractor
) : AppMenuFragmentViewModel, BaseFragmentViewModelImpl() {
    private val dataLiveData = MutableLiveDataWithState<AppMenuFragmentData>(
        initialValue = null
    )

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun setItem(item: AppMenuItem) {
        profileInteractor.getMe()?.let { me ->
            dataLiveData.updateValue { oldValue ->
                (oldValue ?: getInitialData(me = me)).copy(item = item)
            }
        }
    }

    private fun getInitialData(me: StaffMember): AppMenuFragmentData =
        AppMenuFragmentData(
            item = AppMenuItem.MONITORING,
            me = me,
            hasMonitoringAlerts = alertsMonitoringInteractor.haveAlerts(),
            hasProfileAlerts = alertsProfileInteractor.haveAlerts()
        )
}