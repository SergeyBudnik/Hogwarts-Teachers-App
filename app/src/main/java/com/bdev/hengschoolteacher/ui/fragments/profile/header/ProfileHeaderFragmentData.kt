package com.bdev.hengschoolteacher.ui.fragments.profile.header

import com.bdev.hengschoolteacher.ui.fragments.profile.header.data.ProfileHeaderFragmentItem

data class ProfileHeaderFragmentData(
    val currentItem: ProfileHeaderFragmentItem,
    val hasLessonsAlert: Boolean,
    val hasPaymentsAlert: Boolean,
    val hasDebtsAlert: Boolean
)