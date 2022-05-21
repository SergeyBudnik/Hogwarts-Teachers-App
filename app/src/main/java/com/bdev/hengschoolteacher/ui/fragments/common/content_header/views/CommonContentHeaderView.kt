package com.bdev.hengschoolteacher.ui.fragments.common.content_header.views

import com.bdev.hengschoolteacher.ui.fragments.common.content_header.CommonContentHeaderFragmentData

interface CommonContentHeaderView<TabType> {
    fun bind(
        data: CommonContentHeaderFragmentData<TabType>,
        tabClickedAction: (TabType) -> Unit
    )
}