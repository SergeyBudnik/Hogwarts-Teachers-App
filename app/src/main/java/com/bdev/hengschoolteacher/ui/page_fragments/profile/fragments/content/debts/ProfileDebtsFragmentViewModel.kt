package com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.debts

import com.bdev.hengschoolteacher.data.common.NullableMutableLiveDataWithState
import com.bdev.hengschoolteacher.interactors.profile.ProfileInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_debts.StudentsDebtsInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.profile.data.ProfilePageFragmentTab
import com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.ProfileContentFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.ProfileContentFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface ProfileDebtsFragmentViewModel : ProfileContentFragmentViewModel<ProfileDebtsFragmentData>

@HiltViewModel
class ProfileDebtsFragmentViewModelImpl @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val studentsStorageInteractor: StudentsStorageInteractor,
    private val studentsDebtsInteractor: StudentsDebtsInteractor
): ProfileDebtsFragmentViewModel, ProfileContentFragmentViewModelImpl<ProfileDebtsFragmentData>() {
    private val initialData = getInitialData()

    private val dataLiveData = NullableMutableLiveDataWithState(
        initialValue = initialData
    )

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun isVisible() = dataLiveData.getValue()?.visible ?: false

    override fun setCurrentTab(tab: ProfilePageFragmentTab) {
        dataLiveData.updateValue(defaultValue = initialData) { oldValue ->
            oldValue.copy(visible = (tab == ProfilePageFragmentTab.DEBTS))
        }
    }

    private fun getInitialData(): ProfileDebtsFragmentData =
        profileInteractor.getMe()?.let { me ->
            ProfileDebtsFragmentData(
                visible = false,
                studentsToExpectedDebt = studentsStorageInteractor.getAll()
                    .filter { it.managerLogin == me.login }
                    .map {
                        Pair(it, studentsDebtsInteractor.getExpectedDebt(studentLogin = it.login))
                    }
            )
        } ?: throw RuntimeException()

}