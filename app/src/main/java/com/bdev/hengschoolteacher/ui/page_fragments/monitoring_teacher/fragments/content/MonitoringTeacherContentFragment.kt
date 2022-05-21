package com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.fragments.content

import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.ui.fragments.common.content.BaseContentFragment
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.MonitoringPageFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.MonitoringTeacherPageFragmentArguments
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.MonitoringTeacherPageFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.data.MonitoringTeacherPageFragmentTab

abstract class MonitoringTeacherContentFragment<
    ViewModelType : MonitoringTeacherContentFragmentViewModel<*>
> : BaseContentFragment<
    MonitoringTeacherPageFragmentTab,
    MonitoringTeacherPageFragmentArguments,
    ViewModelType
>() {
    final override fun providePageViewModel() =
        ViewModelProvider(requireParentFragment()).get(MonitoringTeacherPageFragmentViewModelImpl::class.java)
}