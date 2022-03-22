package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.lessons

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringLessonsPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class MonitoringLessonsPageFragmentViewModelImpl @Inject constructor(
): MonitoringLessonsPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}