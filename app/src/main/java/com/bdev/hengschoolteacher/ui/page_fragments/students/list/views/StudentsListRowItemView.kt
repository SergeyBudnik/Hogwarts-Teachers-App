package com.bdev.hengschoolteacher.ui.page_fragments.students.list.views

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.student.Student
import kotlinx.android.synthetic.main.view_students_list_row_item.view.*

class StudentsListRowItemView : RelativeLayout {
    init {
        inflate(context, R.layout.view_students_list_row_item, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(student: Student): StudentsListRowItemView {
        studentNameView.text = student.person.name

        return this
    }
}