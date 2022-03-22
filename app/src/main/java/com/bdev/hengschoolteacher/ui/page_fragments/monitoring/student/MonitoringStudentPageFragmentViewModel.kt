package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.student

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringStudentPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class MonitoringStudentPageFragmentViewModelImpl @Inject constructor(
): MonitoringStudentPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}