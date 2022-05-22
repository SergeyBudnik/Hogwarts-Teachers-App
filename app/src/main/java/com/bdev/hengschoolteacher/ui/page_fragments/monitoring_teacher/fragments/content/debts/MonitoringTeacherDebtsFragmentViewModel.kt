package com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.fragments.content.debts

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringTeacherDebtsFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class MonitoringTeacherDebtsFragmentViewModelImpl @Inject constructor(
): MonitoringTeacherDebtsFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}