package com.bdev.hengschoolteacher.ui.views.app.monitoring.teacher

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.services.alerts.monitoring.AlertsMonitoringTeachersService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.teacher.MonitoringTeacherDebtsActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.teacher.MonitoringTeacherLessonsActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.teacher.MonitoringTeacherPaymentsActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.teacher.MonitoringTeacherSalaryActivity
import kotlinx.android.synthetic.main.view_monitoring_teacher_header.view.*
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_monitoring_teacher_header)
open class MonitoringTeacherHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    enum class Item {
        LESSONS, SALARY, PAYMENTS, DEBTS;
    }

    @Bean
    lateinit var alertsMonitoringTeachersService: AlertsMonitoringTeachersService

    fun bind(currentItem: Item, teacherLogin: String) {
        monitoringTeacherHeaderLessonsView.bind(
            active = currentItem == Item.LESSONS,
            hasAlert = alertsMonitoringTeachersService.haveLessonsAlerts(teacherLogin = teacherLogin),
            clickAction = {
                MonitoringTeacherLessonsActivity.redirectToSibling(
                        current = context as BaseActivity,
                        teacherLogin = teacherLogin
                )
            }
        )

        monitoringTeacherHeaderSalaryView.bind(
            active = currentItem == Item.SALARY,
            hasAlert = false,
            clickAction = {
                MonitoringTeacherSalaryActivity.redirectToSibling(
                        current = context as BaseActivity,
                        teacherLogin = teacherLogin
                )
            }
        )

        monitoringTeacherHeaderPaymentsView.bind(
            active = currentItem == Item.PAYMENTS,
            hasAlert = alertsMonitoringTeachersService.havePaymentsAlerts(teacherLogin = teacherLogin),
            clickAction = {
                MonitoringTeacherPaymentsActivity.redirectToSibling(
                        current = context as BaseActivity,
                        teacherLogin = teacherLogin
                )
            }
        )

        monitoringTeacherHeaderDebtsView.bind(
                active = currentItem == Item.DEBTS,
                hasAlert = alertsMonitoringTeachersService.haveDebtsAlerts(teacherLogin = teacherLogin),
                clickAction = {
                    MonitoringTeacherDebtsActivity.redirectToSibling(
                            current = context as BaseActivity,
                            teacherLogin = teacherLogin
                    )
                }
        )
    }
 }
