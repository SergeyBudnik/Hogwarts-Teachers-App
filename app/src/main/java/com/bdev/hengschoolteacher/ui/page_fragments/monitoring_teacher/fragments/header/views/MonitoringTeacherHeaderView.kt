package com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.fragments.header.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.CommonContentHeaderFragmentData
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.views.CommonContentHeaderView
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.data.MonitoringTeacherPageFragmentTab
import kotlinx.android.synthetic.main.view_monitoring_teacher_header.view.*

class MonitoringTeacherHeaderView(
    context: Context,
    attrs: AttributeSet
): LinearLayout(context, attrs), CommonContentHeaderView<MonitoringTeacherPageFragmentTab> {
    init {
        View.inflate(context, R.layout.view_monitoring_teacher_header, this)
    }

    override fun bind(
        data: CommonContentHeaderFragmentData<MonitoringTeacherPageFragmentTab>,
        tabClickedAction: (MonitoringTeacherPageFragmentTab) -> Unit
    ) {
        listOf(
            Pair(monitoringTeacherHeaderLessonsView, MonitoringTeacherPageFragmentTab.LESSONS),
            Pair(monitoringTeacherHeaderSalaryView, MonitoringTeacherPageFragmentTab.SALARY),
            Pair(monitoringTeacherHeaderPaymentsView, MonitoringTeacherPageFragmentTab.PAYMENTS),
            Pair(monitoringTeacherHeaderDebtsView, MonitoringTeacherPageFragmentTab.DEBTS)
        ).forEach { (view, tab) ->
            view.bind(
                active = data.tab == tab,
                hasAlert = data.tabAlerts[tab] ?: false,
                clickAction = { tabClickedAction(tab) }
            )
        }
    }
 }
