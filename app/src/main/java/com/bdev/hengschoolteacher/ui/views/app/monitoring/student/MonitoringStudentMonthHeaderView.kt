package com.bdev.hengschoolteacher.ui.views.app.monitoring.student

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.student.month_payments.MonitoringStudentMonthPaymentsPageFragment
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
//                    MonitoringStudentMonthAttendancePageFragment.redirectToSibling(
//                            current = context as BasePageFragment,
//                            studentLogin = studentLogin,
//                            monthIndex = monthIndex
//                    )
                }
        )

        monitoringStudentMonthHeaderItemPaymentsView.bind(
                active = item == Item.PAYMENTS,
                hasAlert = false,
                clickAction = {
//                    MonitoringStudentMonthPaymentsPageFragment.redirectToSibling(
//                            current = context as BasePageFragment,
//                            studentLogin = studentLogin,
//                            monthIndex = monthIndex
//                    )
                }
        )

        return this
    }
}
