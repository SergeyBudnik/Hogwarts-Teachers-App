package com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.salary

import com.bdev.hengschoolteacher.data.common.MutableLiveDataWithState
import com.bdev.hengschoolteacher.interactors.profile.ProfileInteractor
import com.bdev.hengschoolteacher.interactors.teachers.TeacherSalaryInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.profile.data.ProfilePageFragmentTab
import com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.ProfileContentFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.ProfileContentFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface ProfileSalaryFragmentViewModel : ProfileContentFragmentViewModel<ProfileSalaryFragmentData> {
    fun setWeekIndex(weekIndex: Int)
}

@HiltViewModel
class ProfileSalaryFragmentViewModelImpl @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val teacherSalaryInteractor: TeacherSalaryInteractor,
): ProfileSalaryFragmentViewModel, ProfileContentFragmentViewModelImpl<ProfileSalaryFragmentData>() {
    private val dataLiveData = MutableLiveDataWithState(
        initialValue = getInitialData()
    )

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun isVisible() = dataLiveData.getValue()?.visible ?: false

    override fun setCurrentTab(tab: ProfilePageFragmentTab) {
        dataLiveData.updateValue(defaultValue = getInitialData()) { oldValue ->
            oldValue.copy(visible = (tab == ProfilePageFragmentTab.SALARY))
        }
    }

    override fun setWeekIndex(weekIndex: Int) {
        dataLiveData.updateValue(defaultValue = getInitialData()) { oldValue ->
            oldValue.copy(
                weekIndex = weekIndex,
                payments = teacherSalaryInteractor.getTeacherPayments(
                    teacherLogin = oldValue.me.login,
                    weekIndex = weekIndex
                )
            )
        }
    }

    override fun onHeaderButton1Clicked() {
        dataLiveData.updateValue(defaultValue = getInitialData()) { oldData ->
            oldData.copy(calendarEnabled = !oldData.calendarEnabled)
        }
    }

    private fun getInitialData(): ProfileSalaryFragmentData =
        profileInteractor.getMe()?.let { me ->
            ProfileSalaryFragmentData(
                visible = false,
                calendarEnabled = false,
                weekIndex = 0,
                me = me,
                payments = teacherSalaryInteractor.getTeacherPayments(
                    teacherLogin = me.login,
                    weekIndex = 0
                )
            )
        } ?: throw RuntimeException()

}