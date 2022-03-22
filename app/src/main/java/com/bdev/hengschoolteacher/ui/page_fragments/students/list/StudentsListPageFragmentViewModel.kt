package com.bdev.hengschoolteacher.ui.page_fragments.students.list

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface StudentsListPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class StudentsListPageFragmentViewModelImpl @Inject constructor(
): StudentsListPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}