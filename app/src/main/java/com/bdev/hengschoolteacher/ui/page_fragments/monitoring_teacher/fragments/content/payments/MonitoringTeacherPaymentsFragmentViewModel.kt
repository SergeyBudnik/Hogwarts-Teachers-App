package com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.fragments.content.payments

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringTeacherPaymentsFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class MonitoringTeacherPaymentsFragmentViewModelImpl @Inject constructor(
): MonitoringTeacherPaymentsFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}