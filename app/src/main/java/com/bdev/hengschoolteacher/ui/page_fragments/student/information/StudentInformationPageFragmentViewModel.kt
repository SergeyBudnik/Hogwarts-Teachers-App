package com.bdev.hengschoolteacher.ui.page_fragments.student.information

import androidx.lifecycle.LiveData
import com.bdev.hengschoolteacher.data.common.MutableLiveDataWithState
import com.bdev.hengschoolteacher.data.school.education_info.EducationAge
import com.bdev.hengschoolteacher.data.school.education_info.EducationInfo
import com.bdev.hengschoolteacher.data.school.education_info.EducationLevel
import com.bdev.hengschoolteacher.data.school.person.Person
import com.bdev.hengschoolteacher.data.school.person.PersonContacts
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student.StudentStatusType
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
    private val dataLiveData = MutableLiveDataWithState(
        getInitialState()
    )

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun init(login: String) {
        dataLiveData.updateValue { oldValue ->
            oldValue.copy(
                student = studentsStorageInteractor.getByLogin(
                    studentLogin = login
                ) ?: getEmptyStudent()
            )
        }
    }

    override fun goBack() {
        navigate(
            navCommand = NavCommand.back()
        )
    }

    private fun getInitialState(): StudentInformationPageFragmentData =
        StudentInformationPageFragmentData(
            student = getEmptyStudent()
        )

    private fun getEmptyStudent(): Student =
        Student(
            login = "",
            person = Person(
                name = "",
                contacts = PersonContacts(
                    phones = emptyList(),
                    vkLinks = emptyList()
                )
            ),
            managerLogin = "",
            educationInfo = EducationInfo(
                age = EducationAge.ADULT,
                level = EducationLevel.ADVANCED
            ),
            statusType = StudentStatusType.STUDYING,
            studentGroups = emptyList()
        )
}