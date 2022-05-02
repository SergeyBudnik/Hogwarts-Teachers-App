package com.bdev.hengschoolteacher.ui.fragments.monitoring.header

import com.bdev.hengschoolteacher.ui.fragments.monitoring.header.data.MonitoringHeaderFragmentItem

data class MonitoringHeaderFragmentData(
    val currentItem: MonitoringHeaderFragmentItem,
    val hasLessonsAlert: Boolean,
    val hasTeachersAlert: Boolean,
    val hasStudentsAlert: Boolean
)