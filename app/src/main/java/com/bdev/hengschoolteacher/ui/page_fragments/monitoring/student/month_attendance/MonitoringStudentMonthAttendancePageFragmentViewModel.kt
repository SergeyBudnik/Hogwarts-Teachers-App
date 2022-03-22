package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.student.month_attendance

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringStudentMonthAttendancePageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class MonitoringStudentMonthAttendancePageFragmentViewModelImpl @Inject constructor(
): MonitoringStudentMonthAttendancePageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}