package com.bdev.hengschoolteacher.ui.page_fragments.teacher

import androidx.lifecycle.LiveData
import com.bdev.hengschoolteacher.data.common.NullableMutableLiveDataWithState
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface TeacherPageFragmentViewModel : BasePageFragmentViewModel {
    fun getDataLiveData(): LiveData<TeacherPageFragmentData>

    fun init(teacherLogin: String)
}

@HiltViewModel
class TeacherPageFragmentViewModelImpl @Inject constructor(
    private val staffMembersStorageInteractor: StaffMembersStorageInteractor
): TeacherPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    private val dataLiveData = NullableMutableLiveDataWithState<TeacherPageFragmentData>(
        initialValue = null
    )

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun init(teacherLogin: String) {
        staffMembersStorageInteractor.getStaffMember(login = teacherLogin)?.let { teacher ->
            dataLiveData.updateValue {
                TeacherPageFragmentData(
                    teacher = teacher
                )
            }
        }
    }

    override fun goBack() {
        navigate(
            navCommand = NavCommand.back()
        )
    }
}