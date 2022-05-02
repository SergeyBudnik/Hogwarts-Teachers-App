package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.teachers.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.teachers.data.MonitoringTeachersItemData
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.teachers.views.MonitoringTeachersItemView

class MonitoringTeachersListAdapter(context: Context) : BaseItemsListAdapter<MonitoringTeachersItemData>(context) {
    override fun getView(position: Int, convertView: View?, parentView: ViewGroup): View {
        return if (convertView == null) {
            MonitoringTeachersItemView(context)
        } else {
            convertView as MonitoringTeachersItemView
        }.bind(getItem(position))
    }
}