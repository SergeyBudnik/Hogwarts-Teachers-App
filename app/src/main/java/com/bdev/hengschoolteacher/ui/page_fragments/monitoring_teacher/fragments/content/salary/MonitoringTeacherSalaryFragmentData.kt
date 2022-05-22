package com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.fragments.content.salary

import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.data.school.teacher.TeacherPayment
import com.bdev.hengschoolteacher.ui.fragments.common.content.BaseContentFragmentData
import com.bdev.hengschoolteacher.ui.views.app.header.data.AppHeaderButton
import com.bdev.hengschoolteacher.ui.views.app.header.data.AppHeaderButtons

data class MonitoringTeacherSalaryFragmentData(
    val visible: Boolean,
    val calendarEnabled: Boolean,
    val teacher: StaffMember,
    val payments: List<TeacherPayment>
): BaseContentFragmentData {
    override fun isVisible() = visible

    override fun getHeaderButtons() = AppHeaderButtons(
        button1 = AppHeaderButton(
            iconId = R.drawable.ic_calendar,
            toggled = calendarEnabled
        ),
        button2 = AppHeaderButton(
            iconId = R.drawable.ic_teacher,
            toggled = false
        ),
        button3 = null
    )
}