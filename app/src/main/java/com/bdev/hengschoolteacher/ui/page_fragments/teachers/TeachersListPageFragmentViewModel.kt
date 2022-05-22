package com.bdev.hengschoolteacher.ui.page_fragments.teachers

import androidx.lifecycle.LiveData
import com.bdev.hengschoolteacher.NavGraphDirections
import com.bdev.hengschoolteacher.data.common.NullableMutableLiveDataWithState
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.teacher.TeacherPageFragmentArguments
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface TeachersListPageFragmentViewModel : BasePageFragmentViewModel {
    fun getDataLiveData(): LiveData<TeachersListPageFragmentData>

    fun openTeacherPage(teacher: StaffMember)
}

@HiltViewModel
class TeachersListPageFragmentViewModelImpl @Inject constructor(
    private val staffMembersStorageInteractor: StaffMembersStorageInteractor
): TeachersListPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    private val dataLiveData = NullableMutableLiveDataWithState(
        initialValue = getInitialData()
    )

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun openTeacherPage(teacher: StaffMember) {
        navigate(
            navCommand = NavCommand.forward(
                navDir = NavGraphDirections.teachersListToTeacherAction(
                    args = TeacherPageFragmentArguments(
                        login = teacher.login
                    )
                )
            )
        )
    }

    override fun goBack() {
        navigate(
            navCommand = NavCommand.top(
                navDir = NavGraphDirections.teachersListToProfile()
            )
        )
    }

    private fun getInitialData(): TeachersListPageFragmentData =
        TeachersListPageFragmentData(
            teachers = staffMembersStorageInteractor.getAllStaffMembers()
        )
}