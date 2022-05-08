package com.bdev.hengschoolteacher.ui.page_fragments.students

import com.bdev.hengschoolteacher.NavGraphDirections
import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.student.information.StudentInformationPageFragmentArguments
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface StudentsListPageFragmentViewModel : BasePageFragmentViewModel {
    fun openStudentInformationPage(login: String)
}

@HiltViewModel
class StudentsListPageFragmentViewModelImpl @Inject constructor(
): StudentsListPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun openStudentInformationPage(login: String) {
        navigate(
            navCommand = NavCommand.forward(
                navDir = NavGraphDirections.studentsListToStudentAction(
                    args = StudentInformationPageFragmentArguments(
                        login = login
                    )
                )
            )
        )
    }

    override fun goBack() {
        navigate(
            navCommand = NavCommand.top(
                navDir = NavGraphDirections.studentsListToProfileAction()
            )
        )
    }
}