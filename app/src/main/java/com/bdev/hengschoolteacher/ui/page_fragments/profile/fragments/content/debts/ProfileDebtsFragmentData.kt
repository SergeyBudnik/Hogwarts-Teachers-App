package com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.debts

import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.ui.fragments.common.content.BaseContentFragmentData
import com.bdev.hengschoolteacher.ui.views.app.header.data.AppHeaderButtons

data class ProfileDebtsFragmentData(
    val visible: Boolean,
    val studentsToExpectedDebt: List<Pair<Student, Int>>
): BaseContentFragmentData {
    override fun isVisible() = visible

    override fun getHeaderButtons() = AppHeaderButtons(
        button1 = null,
        button2 = null,
        button3 = null
    )
}