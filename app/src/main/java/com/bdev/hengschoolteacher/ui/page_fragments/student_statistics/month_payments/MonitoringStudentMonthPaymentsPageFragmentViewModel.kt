package com.bdev.hengschoolteacher.ui.page_fragments.student_statistics.month_payments

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringStudentMonthPaymentsPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class MonitoringStudentMonthPaymentsPageFragmentViewModelImpl @Inject constructor(
): MonitoringStudentMonthPaymentsPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}