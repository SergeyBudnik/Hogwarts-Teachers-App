package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.students

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringStudentsPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class MonitoringStudentsPageFragmentViewModelImpl @Inject constructor(
): MonitoringStudentsPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}