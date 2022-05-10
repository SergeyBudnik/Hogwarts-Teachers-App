package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.students

import com.bdev.hengschoolteacher.ui.fragments.common.content.BaseContentFragmentData
import com.bdev.hengschoolteacher.ui.views.app.header.data.AppHeaderButtons

data class MonitoringStudentsFragmentData(
    val visible: Boolean
): BaseContentFragmentData {
    override fun isVisible() = visible

    override fun getHeaderButtons() = AppHeaderButtons(
        button1 = null,
        button2 = null,
        button3 = null
    )
}