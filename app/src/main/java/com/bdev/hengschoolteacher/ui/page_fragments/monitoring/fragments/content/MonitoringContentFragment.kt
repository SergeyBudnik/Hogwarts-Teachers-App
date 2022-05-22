package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content

import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.ui.fragments.common.content.BaseContentFragment
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.MonitoringPageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.MonitoringPageFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.data.MonitoringPageFragmentTab
import com.bdev.hengschoolteacher.ui.page_fragments.profile.ProfilePageFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.profile.data.ProfilePageFragmentTab

abstract class MonitoringContentFragment<
    ViewModelType : MonitoringContentFragmentViewModel<*>
> : BaseContentFragment<
    MonitoringPageFragmentTab,
    Any,
    ViewModelType
>() {
    final override fun providePageViewModel() =
        ViewModelProvider(requireParentFragment()).get(MonitoringPageFragmentViewModelImpl::class.java)
}