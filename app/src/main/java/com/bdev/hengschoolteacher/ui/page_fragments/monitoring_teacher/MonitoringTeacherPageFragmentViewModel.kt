package com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher

import com.bdev.hengschoolteacher.ui.page_fragments.common.content.BaseContentPageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.common.content.BaseContentPageFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.data.MonitoringTeacherPageFragmentTab
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringTeacherPageFragmentViewModel : BaseContentPageFragmentViewModel<
    MonitoringTeacherPageFragmentTab,
    MonitoringTeacherPageFragmentArguments
>

@HiltViewModel
class MonitoringTeacherPageFragmentViewModelImpl @Inject constructor():
    MonitoringTeacherPageFragmentViewModel,
    BaseContentPageFragmentViewModelImpl<
        MonitoringTeacherPageFragmentTab,
        MonitoringTeacherPageFragmentArguments
    >(
        defaultTab = MonitoringTeacherPageFragmentTab.LESSONS
    )
{
    override fun goBack() {
        TODO("Not yet implemented")
    }
}