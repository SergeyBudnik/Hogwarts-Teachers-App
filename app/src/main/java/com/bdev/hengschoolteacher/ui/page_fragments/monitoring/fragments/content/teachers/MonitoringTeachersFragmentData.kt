package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.teachers

import com.bdev.hengschoolteacher.ui.fragments.common.content.BaseContentFragmentData
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.teachers.data.MonitoringTeachersItemData
import com.bdev.hengschoolteacher.ui.views.app.header.data.AppHeaderButtons

data class MonitoringTeachersFragmentData(
    val visible: Boolean,
    val teachers: List<MonitoringTeachersItemData>
): BaseContentFragmentData {
    override fun isVisible() = visible

    override fun getHeaderButtons() = AppHeaderButtons(
        button1 = null,
        button2 = null,
        button3 = null
    )
}