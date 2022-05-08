package com.bdev.hengschoolteacher.ui.page_fragments.students.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.page_fragments.students.views.StudentsListRowItemView

class StudentsListAdapter(context: Context) : BaseItemsListAdapter<Student>(context) {
    override fun getView(position: Int, convertView: View?, parentView: ViewGroup): View {
        return if (convertView == null) {
            StudentsListRowItemView(context)
        } else {
            convertView as StudentsListRowItemView
        }.bind(getItem(position))
    }
}