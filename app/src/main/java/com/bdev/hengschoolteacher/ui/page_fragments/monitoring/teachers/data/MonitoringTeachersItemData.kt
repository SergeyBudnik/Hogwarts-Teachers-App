package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.teachers.data

import com.bdev.hengschoolteacher.data.school.staff.StaffMember

data class MonitoringTeachersItemData(
    val staffMember: StaffMember,
    val hasAlerts: Boolean
)