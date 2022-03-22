package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.teacher.salary

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringTeacherSalaryPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class MonitoringTeacherSalaryPageFragmentViewModelImpl @Inject constructor(
): MonitoringTeacherSalaryPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}