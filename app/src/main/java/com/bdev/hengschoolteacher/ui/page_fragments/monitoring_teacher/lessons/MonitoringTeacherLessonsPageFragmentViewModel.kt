package com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.lessons

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringTeacherLessonsPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class MonitoringTeacherLessonsPageFragmentViewModelImpl @Inject constructor(
): MonitoringTeacherLessonsPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}