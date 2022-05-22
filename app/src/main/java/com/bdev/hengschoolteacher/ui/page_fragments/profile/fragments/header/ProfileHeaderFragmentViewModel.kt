package com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.header

import com.bdev.hengschoolteacher.interactors.alerts.profile.AlertsProfileInteractor
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.CommonContentHeaderFragmentData
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.CommonContentHeaderFragmentViewModel
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.CommonContentHeaderFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.profile.data.ProfilePageFragmentTab
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface ProfileHeaderFragmentViewModel : CommonContentHeaderFragmentViewModel<ProfilePageFragmentTab>

@HiltViewModel
class ProfileHeaderFragmentViewModelImpl @Inject constructor(
    alertsProfileService: AlertsProfileInteractor
): ProfileHeaderFragmentViewModel, CommonContentHeaderFragmentViewModelImpl<ProfilePageFragmentTab>(
    initialData = CommonContentHeaderFragmentData(
        tab = ProfilePageFragmentTab.NONE,
        tabAlerts = mapOf(
            Pair(ProfilePageFragmentTab.LESSONS, alertsProfileService.haveLessonsAlerts()),
            Pair(ProfilePageFragmentTab.PAYMENTS, alertsProfileService.havePaymentsAlerts()),
            Pair(ProfilePageFragmentTab.DEBTS, alertsProfileService.haveDebtsAlerts()),
        )
    )
)