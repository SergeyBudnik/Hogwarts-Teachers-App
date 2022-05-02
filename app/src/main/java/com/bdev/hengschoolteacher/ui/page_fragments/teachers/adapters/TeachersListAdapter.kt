package com.bdev.hengschoolteacher.ui.page_fragments.teachers.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.ui.page_fragments.teachers.views.TeachersListRowItemView

class TeachersListAdapter(
    private val context: Context
) : BaseAdapter() {
    private var teachers: List<StaffMember> = ArrayList()

    fun setTeachers(teachers: List<StaffMember>) {
        this.teachers = teachers.sortedBy { it.login }
    }

    override fun getView(position: Int, convertView: View?, parentView: ViewGroup): View {
        return if (convertView == null) {
            TeachersListRowItemView(context)
        } else {
            convertView as TeachersListRowItemView
        }.bind(getItem(position))
    }

    override fun getItem(position: Int): StaffMember {
        return teachers[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return teachers.size
    }
}