package com.bdev.hengschoolteacher.ui.fragments.common.content_header

data class CommonContentHeaderFragmentData<TabType>(
    val tab: TabType,
    val tabAlerts: Map<TabType, Boolean>
)