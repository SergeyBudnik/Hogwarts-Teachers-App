package com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.debts

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringTeacherDebtsPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class MonitoringTeacherDebtsPageFragmentViewModelImpl @Inject constructor(
): MonitoringTeacherDebtsPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}