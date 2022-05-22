package com.bdev.hengschoolteacher.ui.page_fragments.teachers.views

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import kotlinx.android.synthetic.main.view_teachers_list_row_item.view.*

class TeachersListRowItemView : RelativeLayout {
    init {
        inflate(context, R.layout.view_teachers_list_row_item, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(staffMember: StaffMember): TeachersListRowItemView {
        teachersNameView.text = staffMember.person.name

        return this
    }
}