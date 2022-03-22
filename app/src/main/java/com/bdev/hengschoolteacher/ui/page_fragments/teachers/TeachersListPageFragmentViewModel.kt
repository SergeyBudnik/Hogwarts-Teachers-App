package com.bdev.hengschoolteacher.ui.page_fragments.teachers

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface TeachersListPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class TeachersListPageFragmentViewModelImpl @Inject constructor(
): TeachersListPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}