package com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.header

import androidx.lifecycle.LiveData
import com.bdev.hengschoolteacher.data.common.MutableLiveDataWithState
import com.bdev.hengschoolteacher.interactors.alerts.profile.AlertsProfileInteractor
import com.bdev.hengschoolteacher.ui.fragments.BaseFragmentViewModel
import com.bdev.hengschoolteacher.ui.fragments.BaseFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.profile.data.ProfilePageFragmentTab
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface ProfileHeaderFragmentViewModel : BaseFragmentViewModel {
    fun getDataLiveData(): LiveData<ProfileHeaderFragmentData>

    fun setCurrentItem(tab: ProfilePageFragmentTab)
}

@HiltViewModel
class ProfileHeaderFragmentViewModelImpl @Inject constructor(
    private val alertsProfileService: AlertsProfileInteractor
): ProfileHeaderFragmentViewModel, BaseFragmentViewModelImpl() {
    private val dataLiveData = MutableLiveDataWithState(
        getInitialData()
    )

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun setCurrentItem(tab: ProfilePageFragmentTab) {
        dataLiveData.updateValue { oldValue ->
            oldValue?.copy(tab = tab) ?: getInitialData()
        }
    }

    private fun getInitialData(): ProfileHeaderFragmentData =
        ProfileHeaderFragmentData(
            tab = ProfilePageFragmentTab.NONE,
            hasLessonsAlert = alertsProfileService.haveLessonsAlerts(),
            hasPaymentsAlert = alertsProfileService.havePaymentsAlerts(),
            hasDebtsAlert = alertsProfileService.haveDebtsAlerts()
        )
}