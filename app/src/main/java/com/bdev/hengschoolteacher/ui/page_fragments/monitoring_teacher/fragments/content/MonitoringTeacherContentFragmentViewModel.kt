package com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.fragments.content

import com.bdev.hengschoolteacher.ui.fragments.common.content.BaseContentFragmentData
import com.bdev.hengschoolteacher.ui.fragments.common.content.BaseContentFragmentViewModel
import com.bdev.hengschoolteacher.ui.fragments.common.content.BaseContentFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.data.MonitoringPageFragmentTab
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.MonitoringTeacherPageFragmentArguments
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.data.MonitoringTeacherPageFragmentTab
import com.bdev.hengschoolteacher.ui.page_fragments.profile.data.ProfilePageFragmentTab

interface MonitoringTeacherContentFragmentViewModel<
    DataType : BaseContentFragmentData
> : BaseContentFragmentViewModel<
    MonitoringTeacherPageFragmentTab,
    MonitoringTeacherPageFragmentArguments,
    DataType
>

abstract class MonitoringTeacherContentFragmentViewModelImpl<
    DataType :
    BaseContentFragmentData
> : MonitoringTeacherContentFragmentViewModel<
    DataType
>, BaseContentFragmentViewModelImpl<
    MonitoringTeacherPageFragmentTab,
    MonitoringTeacherPageFragmentArguments,
    DataType
>()