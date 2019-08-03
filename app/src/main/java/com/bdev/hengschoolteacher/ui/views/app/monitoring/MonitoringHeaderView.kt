package com.bdev.hengschoolteacher.ui.views.app.monitoring

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.alerts.monitoring.MonitoringAlertsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.MonitoringLessonsActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.MonitoringStudentsActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.MonitoringTeachersActivity
import kotlinx.android.synthetic.main.view_header_monitoring.view.*
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_header_monitoring)
open class MonitoringHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    @Bean
    lateinit var monitoringAlertsService: MonitoringAlertsService

    enum class Item(val id: Int) {
        LESSONS(1), SALARIES(2), PAYMENTS(3);
    }

    fun bind(currentItem: Item) {
        monitoringHeaderLessonsView.setActive(currentItem == Item.LESSONS)
        monitoringHeaderSalariesView.setActive(currentItem == Item.SALARIES)
        monitoringHeaderPaymentsView.setActive(currentItem == Item.PAYMENTS)

        monitoringHeaderLessonsView.setOnClickListener {
            MonitoringLessonsActivity.redirectToSibling(context as BaseActivity)
        }

        // ToDo: salaries -> teachers
        monitoringHeaderSalariesView.setOnClickListener {
            MonitoringTeachersActivity.redirectToSibling(context as BaseActivity)
        }

        // ToDo: payments -> students
        monitoringHeaderPaymentsView.setOnClickListener {
            MonitoringStudentsActivity.redirectToSibling(context as BaseActivity)
        }

        if (monitoringAlertsService.lessonsHaveAlerts()) {
            monitoringHeaderLessonsView.setIcon(
                    iconId = R.drawable.ic_alert,
                    colorId = R.color.fill_text_basic_negative
            )
        }

        if (monitoringAlertsService.teachersHaveAlerts()) {
            monitoringHeaderSalariesView.setIcon(
                    iconId = R.drawable.ic_alert,
                    colorId = R.color.fill_text_basic_negative
            )
        }

        if (monitoringAlertsService.studentsHaveAlerts()) {
            monitoringHeaderPaymentsView.setIcon(
                    iconId = R.drawable.ic_alert,
                    colorId = R.color.fill_text_basic_negative
            )
        }
    }
}

