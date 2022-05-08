package com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.payments

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringTeacherPaymentsPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class MonitoringTeacherPaymentsPageFragmentViewModelImpl @Inject constructor(
): MonitoringTeacherPaymentsPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}