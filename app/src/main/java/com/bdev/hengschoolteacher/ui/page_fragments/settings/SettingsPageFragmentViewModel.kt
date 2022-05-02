package com.bdev.hengschoolteacher.ui.page_fragments.settings

import androidx.lifecycle.LiveData
import com.bdev.hengschoolteacher.NavGraphDirections
import com.bdev.hengschoolteacher.data.common.MutableLiveDataWithState
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractor
import com.bdev.hengschoolteacher.interactors.profile.ProfileInteractor
import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface SettingsPageFragmentViewModel : BasePageFragmentViewModel {
    fun getDataLiveData(): LiveData<SettingsPageFragmentData>

    fun logout()
}

@HiltViewModel
class SettingsPageFragmentViewModelImpl @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val authStorageInteractor: AuthStorageInteractor
): SettingsPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    private val dataLiveData = MutableLiveDataWithState(
        getInitialData()
    )

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun logout() {
        authStorageInteractor.clearAuthInfo()

        navigate(
            navCommand = NavCommand.top(
                navDir = NavGraphDirections.settingsToLogin()
            )
        )
    }

    override fun goBack() {
        navigate(
            navCommand = NavCommand.top(
                navDir = NavGraphDirections.settingsToProfile()
            )
        )
    }

    private fun getInitialData(): SettingsPageFragmentData =
        profileInteractor.getMe().let { me ->
            SettingsPageFragmentData(
                name = me?.person?.name ?: "",
                login = me?.login ?: ""
            )
        }
}