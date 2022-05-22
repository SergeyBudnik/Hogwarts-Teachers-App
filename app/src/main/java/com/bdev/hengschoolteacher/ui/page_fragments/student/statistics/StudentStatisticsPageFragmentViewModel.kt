package com.bdev.hengschoolteacher.ui.page_fragments.student.statistics

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface StudentStatisticsPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class StudentStatisticsPageFragmentViewModelImpl @Inject constructor(
): StudentStatisticsPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}