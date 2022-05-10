package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.header

import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.data.MonitoringPageFragmentTab

data class MonitoringHeaderFragmentData(
    val tab: MonitoringPageFragmentTab,
    val hasLessonsAlert: Boolean,
    val hasTeachersAlert: Boolean,
    val hasStudentsAlert: Boolean
)