package com.bdev.hengschoolteacher.ui.page_fragments.common.content

import com.bdev.hengschoolteacher.ui.views.app.header.data.AppHeaderButtons

data class BaseContentPageFragmentData<TabType>(
    val tab: TabType,
    val headerButtons: AppHeaderButtons
)