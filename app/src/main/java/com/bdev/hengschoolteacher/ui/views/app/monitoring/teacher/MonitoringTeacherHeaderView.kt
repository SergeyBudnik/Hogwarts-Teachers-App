package com.bdev.hengschoolteacher.ui.views.app.monitoring.teacher

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.alerts.monitoring.AlertsMonitoringTeachersService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.teacher.MonitoringTeacherLessonsActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.teacher.MonitoringTeacherPaymentsActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.teacher.MonitoringTeacherSalaryActivity
import kotlinx.android.synthetic.main.view_monitoring_teacher_header.view.*
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_monitoring_teacher_header)
open class MonitoringTeacherHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    enum class Item {
        LESSONS, SALARY, PAYMENTS;
    }

    @Bean
    lateinit var alertsMonitoringTeachersService: AlertsMonitoringTeachersService

    fun bind(currentItem: Item, teacherId: Long) {
        monitoringTeacherHeaderLessonsView.let {
            it.setActive(currentItem == Item.LESSONS)
            it.setHasAlert(alertsMonitoringTeachersService.haveLessonsAlerts(teacherId))
            it.setOnClickListener {
                MonitoringTeacherLessonsActivity.redirectToSibling(
                        current = context as BaseActivity,
                        teacherId = teacherId
                )
            }
        }

        monitoringTeacherHeaderSalaryView.let {
            it.setActive(currentItem == Item.SALARY)
            it.setHasAlert(false)
            it.setOnClickListener {
                MonitoringTeacherSalaryActivity.redirectToSibling(
                        current = context as BaseActivity,
                        teacherId = teacherId
                )
            }
        }

        monitoringTeacherHeaderPaymentsView.let {
            it.setActive(currentItem == Item.PAYMENTS)
            it.setHasAlert(alertsMonitoringTeachersService.havePaymentsAlerts(teacherId))
            it.setOnClickListener {
                MonitoringTeacherPaymentsActivity.redirectToSibling(
                        current = context as BaseActivity,
                        teacherId = teacherId
                )
            }
        }
    }
 }
