package com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.header

import com.bdev.hengschoolteacher.ui.page_fragments.profile.data.ProfilePageFragmentTab

data class ProfileHeaderFragmentData(
    val tab: ProfilePageFragmentTab,
    val hasLessonsAlert: Boolean,
    val hasPaymentsAlert: Boolean,
    val hasDebtsAlert: Boolean
)