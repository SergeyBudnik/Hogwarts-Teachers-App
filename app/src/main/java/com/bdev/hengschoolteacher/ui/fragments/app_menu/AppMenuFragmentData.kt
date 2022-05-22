package com.bdev.hengschoolteacher.ui.fragments.app_menu

import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.ui.fragments.app_menu.data.AppMenuItem

data class AppMenuFragmentData(
    val item: AppMenuItem,
    val me: StaffMember,
    val hasProfileAlerts: Boolean,
    val hasMonitoringAlerts: Boolean
)