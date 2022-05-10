package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content

import com.bdev.hengschoolteacher.ui.fragments.common.content.BaseContentFragmentData
import com.bdev.hengschoolteacher.ui.fragments.common.content.BaseContentFragmentViewModel
import com.bdev.hengschoolteacher.ui.fragments.common.content.BaseContentFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.data.MonitoringPageFragmentTab
import com.bdev.hengschoolteacher.ui.page_fragments.profile.data.ProfilePageFragmentTab

interface MonitoringContentFragmentViewModel<DataType : BaseContentFragmentData> :
    BaseContentFragmentViewModel<MonitoringPageFragmentTab, DataType>

abstract class MonitoringContentFragmentViewModelImpl<DataType : BaseContentFragmentData> :
    BaseContentFragmentViewModelImpl<MonitoringPageFragmentTab, DataType>()