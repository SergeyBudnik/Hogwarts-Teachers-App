package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.teachers

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringTeachersPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class MonitoringTeachersPageFragmentViewModelImpl @Inject constructor(
): MonitoringTeachersPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}