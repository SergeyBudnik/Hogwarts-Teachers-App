package com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.lessons

import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.ui.fragments.common.content.BaseContentFragmentData
import com.bdev.hengschoolteacher.ui.views.app.header.data.AppHeaderButton
import com.bdev.hengschoolteacher.ui.views.app.header.data.AppHeaderButtons

data class ProfileLessonsFragmentData(
    val visible: Boolean,
    val lessons: List<GroupAndLesson>,
    val weekIndex: Int,
    val filterEnabled: Boolean,
    val calendarEnabled: Boolean
): BaseContentFragmentData {
    override fun isVisible() = visible

    override fun getHeaderButtons() = AppHeaderButtons(
        button1 = AppHeaderButton(
            iconId = R.drawable.ic_filter,
            toggled = filterEnabled
        ),
        button2 = AppHeaderButton(
            iconId = R.drawable.ic_calendar,
            toggled = calendarEnabled
        ),
        button3 = null
    )
}