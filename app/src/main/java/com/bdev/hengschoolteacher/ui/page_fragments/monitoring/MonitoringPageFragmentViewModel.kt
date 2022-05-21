package com.bdev.hengschoolteacher.ui.page_fragments.monitoring

import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import com.bdev.hengschoolteacher.ui.page_fragments.common.content.BaseContentPageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.common.content.BaseContentPageFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.data.MonitoringPageFragmentTab
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringPageFragmentViewModel : BaseContentPageFragmentViewModel<
    MonitoringPageFragmentTab,
    Any
>

@HiltViewModel
class MonitoringPageFragmentViewModelImpl @Inject constructor():
    MonitoringPageFragmentViewModel,
    BaseContentPageFragmentViewModelImpl<
        MonitoringPageFragmentTab,
        Any
    >(
    defaultTab = MonitoringPageFragmentTab.LESSONS
) {
    override fun goBack() {
        navigate(navCommand = NavCommand.quit()) // todo
    }
}