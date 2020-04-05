package com.bdev.hengschoolteacher.ui.views.app.student

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.student.MonitoringStudentActivity
import com.bdev.hengschoolteacher.ui.activities.student.StudentInformationActivity
import com.bdev.hengschoolteacher.ui.activities.student.StudentPaymentActivity
import com.bdev.hengschoolteacher.ui.views.branded.BrandedSecondaryHeaderItem
import kotlinx.android.synthetic.main.view_student_header.view.*
import org.androidannotations.annotations.EViewGroup

enum class StudentHeaderItem {
    DETAILS, ATTENDANCE, PAYMENTS
}

@EViewGroup(R.layout.view_student_header)
open class StudentHeaderView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(item: StudentHeaderItem, studentLogin: String) {
        initTab(v = studentHeaderDetailsView, isActive = item == StudentHeaderItem.DETAILS) {
            StudentInformationActivity.redirectToSibling(current = getActivity(), studentLogin = studentLogin)
        }

        initTab(v = studentHeaderAttendanceView, isActive = item == StudentHeaderItem.ATTENDANCE) {
            MonitoringStudentActivity.redirectToSibling(current = getActivity(), studentLogin = studentLogin)
        }

        initTab(v = studentHeaderPaymentsView, isActive = item == StudentHeaderItem.PAYMENTS) {
            StudentPaymentActivity.redirectToSibling(current = getActivity(), studentLogin = studentLogin)
        }
    }

    private fun initTab(
            v: BrandedSecondaryHeaderItem,
            isActive: Boolean,
            action: () -> Unit
    ) {
        v.setActive(isActive)
        v.setOnClickListener { action.invoke() }
    }

    private fun getActivity(): BaseActivity {
        return context as BaseActivity
    }
}