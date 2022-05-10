package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.header.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.data.MonitoringPageFragmentTab
import kotlinx.android.synthetic.main.view_header_monitoring.view.*

class MonitoringHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    init {
        View.inflate(context, R.layout.view_header_monitoring, this)
    }

    fun bind(
        currentTab: MonitoringPageFragmentTab,
        hasLessonsAlert: Boolean,
        hasTeachersAlert: Boolean,
        hasStudentsAlert: Boolean,
        tabClickAction: (MonitoringPageFragmentTab) -> Unit
    ) {
        monitoringHeaderLessonsView.bind(
            active = currentTab == MonitoringPageFragmentTab.LESSONS,
            hasAlert = hasLessonsAlert,
            clickAction = { tabClickAction(MonitoringPageFragmentTab.LESSONS) }
        )

        monitoringHeaderTeachersView.bind(
            active = currentTab == MonitoringPageFragmentTab.TEACHERS,
            hasAlert = hasTeachersAlert,
            clickAction = { tabClickAction(MonitoringPageFragmentTab.TEACHERS) }
        )

        monitoringHeaderStudentsView.bind(
            active = currentTab == MonitoringPageFragmentTab.STUDENTS,
            hasAlert = hasStudentsAlert,
            clickAction = { tabClickAction(MonitoringPageFragmentTab.STUDENTS) }
        )
    }
}

