package com.bdev.hengschoolteacher.ui.views.app.monitoring.student

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.student.MonitoringStudentMonthAttendanceActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.student.MonitoringStudentMonthPaymentsActivity
import kotlinx.android.synthetic.main.view_monitoring_student_month_header.view.*
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_monitoring_student_month_header)
open class MonitoringStudentMonthHeaderView : LinearLayout {
    enum class Item {
        ATTENDANCE, PAYMENTS
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(studentId: Long, monthIndex: Int): MonitoringStudentMonthHeaderView {
        monitoringStudentMonthHeaderItemAttendanceView.setOnClickListener {
            MonitoringStudentMonthAttendanceActivity.redirectToSibling(
                    current = context as BaseActivity,
                    studentId = studentId,
                    monthIndex = monthIndex
            )
        }

        monitoringStudentMonthHeaderItemPaymentsView.setOnClickListener {
            MonitoringStudentMonthPaymentsActivity.redirectToSibling(
                    current = context as BaseActivity,
                    studentId = studentId,
                    monthIndex = monthIndex
            )
        }

        return this
    }

    fun setItem(item: Item): MonitoringStudentMonthHeaderView {
        monitoringStudentMonthHeaderItemAttendanceView.setActive(item == Item.ATTENDANCE)
        monitoringStudentMonthHeaderItemPaymentsView.setActive(item == Item.PAYMENTS)

        return this
    }
}
