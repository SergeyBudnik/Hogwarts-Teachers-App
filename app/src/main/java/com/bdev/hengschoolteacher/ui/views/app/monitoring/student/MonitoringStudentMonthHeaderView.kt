package com.bdev.hengschoolteacher.ui.views.app.monitoring.student

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.student.MonitoringStudentMonthAttendanceActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.student.MonitoringStudentMonthPaymentsActivity
import kotlinx.android.synthetic.main.view_monitoring_student_month_header.view.*

class MonitoringStudentMonthHeaderView : LinearLayout {
    enum class Item {
        ATTENDANCE, PAYMENTS
    }

    init {
        View.inflate(context, R.layout.view_monitoring_student_month_header, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(studentLogin: String, monthIndex: Int, item: Item): MonitoringStudentMonthHeaderView {
        monitoringStudentMonthHeaderItemAttendanceView.bind(
                active = item == Item.ATTENDANCE,
                hasAlert = false,
                clickAction = {
                    MonitoringStudentMonthAttendanceActivity.redirectToSibling(
                            current = context as BaseActivity,
                            studentLogin = studentLogin,
                            monthIndex = monthIndex
                    )
                }
        )

        monitoringStudentMonthHeaderItemPaymentsView.bind(
                active = item == Item.PAYMENTS,
                hasAlert = false,
                clickAction = {
                    MonitoringStudentMonthPaymentsActivity.redirectToSibling(
                            current = context as BaseActivity,
                            studentLogin = studentLogin,
                            monthIndex = monthIndex
                    )
                }
        )

        return this
    }
}
