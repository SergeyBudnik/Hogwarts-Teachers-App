package com.bdev.hengschoolteacher.ui.page_fragments.student.information

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface StudentInformationPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class StudentInformationPageFragmentViewModelImpl @Inject constructor(
): StudentInformationPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}