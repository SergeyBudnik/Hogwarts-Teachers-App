package com.bdev.hengschoolteacher.ui.views.app.student

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.student.MonitoringStudentPageFragment
import kotlinx.android.synthetic.main.view_student_header.view.*

enum class StudentHeaderItem {
    DETAILS, ATTENDANCE, PAYMENTS
}

class StudentHeaderView : LinearLayout {
    init {
        View.inflate(context, R.layout.view_student_header, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(item: StudentHeaderItem, studentLogin: String) {
        studentHeaderDetailsView.bind(
                active = item == StudentHeaderItem.DETAILS,
                hasAlert = false,
                clickAction = {
                    // StudentInformationPageFragment.redirectToSibling(current = getActivity(), studentLogin = studentLogin)
                }
        )

        studentHeaderAttendanceView.bind(
                active = item == StudentHeaderItem.ATTENDANCE,
                hasAlert = false,
                clickAction = {
                    // MonitoringStudentPageFragment.redirectToSibling(current = getActivity(), studentLogin = studentLogin)
                }
        )

        studentHeaderPaymentsView.bind(
                active = item == StudentHeaderItem.PAYMENTS,
                hasAlert = false,
                clickAction = {
                    // StudentPaymentPageFragment.redirectToSibling(current = getActivity(), studentLogin = studentLogin)
                }
        )
    }
}