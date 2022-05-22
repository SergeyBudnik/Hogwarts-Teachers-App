package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.header.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.CommonContentHeaderFragmentData
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.views.CommonContentHeaderView
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.data.MonitoringPageFragmentTab
import kotlinx.android.synthetic.main.view_header_monitoring.view.*

class MonitoringHeaderView(
    context: Context,
    attrs: AttributeSet
): LinearLayout(context, attrs), CommonContentHeaderView<MonitoringPageFragmentTab> {
    init {
        View.inflate(context, R.layout.view_header_monitoring, this)
    }

    override fun bind(
        data: CommonContentHeaderFragmentData<MonitoringPageFragmentTab>,
        tabClickedAction: (MonitoringPageFragmentTab) -> Unit
    ) {
        listOf(
            Pair(monitoringHeaderLessonsView, MonitoringPageFragmentTab.LESSONS),
            Pair(monitoringHeaderTeachersView, MonitoringPageFragmentTab.TEACHERS),
            Pair(monitoringHeaderStudentsView, MonitoringPageFragmentTab.STUDENTS),
        ).forEach { (view, tab) ->
            view.bind(
                active = data.tab == tab,
                hasAlert = data.tabAlerts[tab] ?: false,
                clickAction = { tabClickedAction(tab) }
            )
        }
    }
}

