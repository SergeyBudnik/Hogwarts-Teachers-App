package com.bdev.hengschoolteacher.ui.page_fragments.student.information

import androidx.lifecycle.LiveData
import com.bdev.hengschoolteacher.data.common.NullableMutableLiveDataWithState
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface StudentInformationPageFragmentViewModel : BasePageFragmentViewModel {
    fun getDataLiveData(): LiveData<StudentInformationPageFragmentData>

    fun init(login: String)
}

@HiltViewModel
class StudentInformationPageFragmentViewModelImpl @Inject constructor(
    private val studentsStorageInteractor: StudentsStorageInteractor
): StudentInformationPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    private val dataLiveData = NullableMutableLiveDataWithState<StudentInformationPageFragmentData>(
        initialValue = null
    )

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun init(login: String) {
        studentsStorageInteractor.getByLogin(studentLogin = login)?.let { student ->
            dataLiveData.updateValue {
                StudentInformationPageFragmentData(student = student)
            }
        }
    }

    override fun goBack() {
        navigate(
            navCommand = NavCommand.back()
        )
    }
}