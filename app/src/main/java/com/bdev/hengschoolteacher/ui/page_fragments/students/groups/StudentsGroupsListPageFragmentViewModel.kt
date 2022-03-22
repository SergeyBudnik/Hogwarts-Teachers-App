package com.bdev.hengschoolteacher.ui.page_fragments.students.groups

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface StudentsGroupsListPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class StudentsGroupsListPageFragmentViewModelImpl @Inject constructor(
): StudentsGroupsListPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}