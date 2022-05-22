package com.bdev.hengschoolteacher.ui.page_fragments.common.content

import com.bdev.hengschoolteacher.ui.views.app.header.data.AppHeaderButtons

data class BaseContentPageFragmentData<TabType, ArgsType>(
    val tab: TabType,
    val args: ArgsType,
    val headerButtons: AppHeaderButtons
)