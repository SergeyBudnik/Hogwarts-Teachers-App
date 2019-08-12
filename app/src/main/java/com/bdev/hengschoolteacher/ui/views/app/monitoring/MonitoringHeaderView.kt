package com.bdev.hengschoolteacher.ui.views.app.monitoring

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.alerts.monitoring.AlertsMonitoringService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.MonitoringLessonsActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.MonitoringStudentsActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.MonitoringTeachersActivity
import kotlinx.android.synthetic.main.view_header_monitoring.view.*
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_header_monitoring)
open class MonitoringHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    enum class Item {
        LESSONS, TEACHERS, STUDENTS;
    }

    @Bean
    lateinit var alertsMonitoringService: AlertsMonitoringService

    fun bind(currentItem: Item) {
        monitoringHeaderLessonsView.let {
            it.setActive(currentItem == Item.LESSONS)
            it.setHasAlert(alertsMonitoringService.lessonsHaveAlerts())
            it.setOnClickListener { MonitoringLessonsActivity.redirectToSibling(context as BaseActivity) }
        }

        monitoringHeaderTeachersView.let {
            it.setActive(currentItem == Item.TEACHERS)
            it.setHasAlert(alertsMonitoringService.teachersHaveAlerts())
            it.setOnClickListener { MonitoringTeachersActivity.redirectToSibling(context as BaseActivity) }
        }

        monitoringHeaderStudentsView.let {
            it.setActive(currentItem == Item.STUDENTS)
            it.setHasAlert(alertsMonitoringService.studentsHaveAlerts())
            it.setOnClickListener { MonitoringStudentsActivity.redirectToSibling(context as BaseActivity) }
        }
    }
}

