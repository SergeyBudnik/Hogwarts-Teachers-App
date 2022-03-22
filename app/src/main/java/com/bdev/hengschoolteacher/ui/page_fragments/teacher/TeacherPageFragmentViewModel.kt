package com.bdev.hengschoolteacher.ui.page_fragments.teacher

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface TeacherPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class TeacherPageFragmentViewModelImpl @Inject constructor(
): TeacherPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        TODO("Not yet implemented")
    }
}